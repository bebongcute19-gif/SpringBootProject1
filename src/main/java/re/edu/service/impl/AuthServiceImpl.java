package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.exception.BadCredentialsExceptionCustom;
import re.edu.exception.DuplicateResourceException;
import re.edu.exception.JwtExceptionCustom;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.authReq.UserCreateDTO;
import re.edu.model.dto.request.authReq.UserLoginDTO;
import re.edu.model.dto.request.authReq.VerifyTokenRequest;
import re.edu.model.dto.response.authRes.JwtResponse;
import re.edu.model.dto.response.authRes.VerifyTokenResponse;
import re.edu.model.dto.response.userRes.UserResponse;
import re.edu.model.entity.User;
import re.edu.repository.userRep.UserRepository;
import re.edu.security.jwt.JwtProvider;
import re.edu.service.AuthService;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final ModelMapper modelMapper;

    /**
     * ADMIN tạo tài khoản
     * API:
     * POST /api/users
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse register(UserCreateDTO req) {

        // validate username
        if (req.getUsername() == null || req.getUsername().isBlank()) {

            throw new BadCredentialsExceptionCustom("Tên đăng nhập không được để trống");
        }

        // validate password
        if (req.getPassword() == null || req.getPassword().isBlank()) {

            throw new BadCredentialsExceptionCustom("Mật khẩu không được để trống");
        }

        // validate fullname
        if (req.getFullName() == null || req.getFullName().isBlank()) {

            throw new BadCredentialsExceptionCustom("Họ tên không được để trống");
        }

        // validate email
        if (req.getEmail() == null || req.getEmail().isBlank()) {

            throw new BadCredentialsExceptionCustom("Email không được để trống");
        }

        // validate role
        if (req.getRole() == null) {

            throw new BadCredentialsExceptionCustom("Vai trò không được để trống");
        }

        // duplicate email
        if (userRepository.existsByEmail(req.getEmail())) {

            throw new DuplicateResourceException("Email đã tồn tại");
        }

        // duplicate username
        if (userRepository.existsByUsername(req.getUsername())) {

            throw new DuplicateResourceException("Tên đăng nhập đã tồn tại");
        }

        User newUser = new User();

        newUser.setUsername(req.getUsername());

        newUser.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        newUser.setEmail(req.getEmail());

        newUser.setFullName(req.getFullName());

        newUser.setRole(req.getRole());

        newUser.setIsActive(true);

        User savedUser = userRepository.save(newUser);

        return modelMapper.map(savedUser, UserResponse.class);
    }

    /**
     * Đăng nhập
     * API:
     * POST /api/auth/login
     */
    @Override
    public JwtResponse login(UserLoginDTO req) {

        // validate username
        if (req.getUsername() == null || req.getUsername().isBlank()) {

            throw new BadCredentialsExceptionCustom("Tên đăng nhập không được để trống");
        }

        // validate password
        if (req.getPassword() == null || req.getPassword().isBlank()) {

            throw new BadCredentialsExceptionCustom("Mật khẩu không được để trống");
        }

        User user = userRepository.findUserByUsername(req.getUsername()).orElseThrow(() -> new BadCredentialsExceptionCustom("Tên đăng nhập hoặc mật khẩu không chính xác"));

        // check active
        if (!Boolean.TRUE.equals(user.getIsActive())) {

            throw new AccessDeniedExceptionCustom("Tài khoản đã bị khóa");
        }

        Authentication authentication;

        try {

            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

        } catch (Exception e) {

            throw new BadCredentialsExceptionCustom("Tên đăng nhập hoặc mật khẩu không chính xác");
        }

        String accessToken = jwtProvider.generateAccessToken((UserDetails) authentication.getPrincipal());

        String refreshToken = jwtProvider.generateRefreshToken((UserDetails) authentication.getPrincipal());

        return JwtResponse.builder().username(user.getUsername()).role(user.getRole()).accessToken(accessToken).refreshToken(refreshToken).expirationDate(new Date(System.currentTimeMillis() + 15 * 60 * 1000)).build();
    }

    /**
     * Verify token
     * API:
     * POST /api/auth/verify
     */
    @Override
    public VerifyTokenResponse verifyToken(VerifyTokenRequest req) {

        if (req.getToken() == null || req.getToken().isBlank()) {

            throw new JwtExceptionCustom("Token không được để trống");
        }

        jwtProvider.validateToken(req.getToken());

        String username = jwtProvider.getUsernameFromToken(req.getToken());

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        if (!Boolean.TRUE.equals(user.getIsActive())) {

            throw new AccessDeniedExceptionCustom("Tài khoản đã bị khóa");
        }

        return VerifyTokenResponse.builder().valid(true).username(user.getUsername()).role(user.getRole()).build();
    }

    /**
     * Lấy thông tin user hiện tại
     * API:
     * GET /api/auth/me
     */
    @Override
    @PreAuthorize("hasAnyRole('ADMIN','MENTOR','STUDENT')")
    public UserResponse getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        return modelMapper.map(user, UserResponse.class);
    }
}