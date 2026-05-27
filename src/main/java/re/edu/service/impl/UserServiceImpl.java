package re.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import re.edu.exception.AccessDeniedExceptionCustom;
import re.edu.model.dto.request.authReq.UserCreateDTO;
import re.edu.exception.DuplicateResourceException;
import re.edu.exception.ResourceNotFoundException;
import re.edu.model.dto.request.authReq.UpdatePasswordRequest;
import re.edu.model.dto.request.userReq.UpdateUserRequest;
import re.edu.model.dto.response.userRes.UserResponse;
import re.edu.model.enums.Role;
import re.edu.model.entity.User;
import re.edu.repository.assessmentRep.InternshipAssignmentRepository;
import re.edu.repository.userRep.UserRepository;
import re.edu.security.userDetail.CustomUserDetails;
import re.edu.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;
    private final InternshipAssignmentRepository internshipAssignmentRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers(Role role) {

        List<User> users;

        if (role != null) {

            users = userRepository.findAllByRole(role);

        } else {

            users = userRepository.findAll();
        }

        return users.stream().map(user -> modelMapper.map(user, UserResponse.class)).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(Integer userId) {

        User user = userRepository.getUserById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(Integer userId, UpdateUserRequest req) {

        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        // update fullName nếu có gửi
        if (req.getFullName() != null && !req.getFullName().isBlank()) {

            user.setFullName(req.getFullName());
        }

        // update email nếu có gửi
        if (req.getEmail() != null && !req.getEmail().isBlank()) {

            // check duplicate email
            if (!user.getEmail().equals(req.getEmail())
                    && userRepository.existsByEmail(req.getEmail())) {

                throw new DuplicateResourceException("Email đã tồn tại");
            }

            user.setEmail(req.getEmail());
        }

        // update phoneNumber nếu có gửi
        if (req.getPhoneNumber() != null
                && !req.getPhoneNumber().isBlank()) {

            user.setPhoneNumber(req.getPhoneNumber());
        }

        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserPassword(Integer userId, UpdatePasswordRequest req) {

        User targetUser = getTargetUser(userId);

        targetUser.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));

        userRepository.save(targetUser);

        return "Cập nhật mật khẩu thành công";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserStatus(Integer userId, Boolean status) {

        User targetUser = getTargetUser(userId);

        targetUser.setIsActive(status);

        userRepository.save(targetUser);

        return status ? "Mở khóa tài khoản thành công" : "Khóa tài khoản thành công";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserRole(
            Integer userId,
            Role role
    ) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy người dùng"
                        )
                );

        /**
         * Không cho đổi quyền ADMIN
         */
        if (user.getRole() == Role.ADMIN) {

            throw new AccessDeniedExceptionCustom(
                    "Không thể thay đổi quyền của ADMIN"
            );
        }

        /**
         * Mentor đã được phân công
         * thì không cho đổi quyền
         */
        if (user.getRole() == Role.MENTOR) {

            boolean assigned =
                    internshipAssignmentRepository
                            .existsByMentor_Id(userId);

            if (assigned) {

                throw new AccessDeniedExceptionCustom(
                        "Giảng viên đã được phân công lớp, không thể đổi quyền"
                );
            }
        }

        /**
         * Student đã được phân công
         * thì không cho đổi quyền
         */
        if (user.getRole() == Role.STUDENT) {

            boolean assigned =
                    internshipAssignmentRepository
                            .existsByStudent_Id(userId);

            if (assigned) {

                throw new AccessDeniedExceptionCustom(
                        "Sinh viên đã được phân công thực tập, không thể đổi quyền"
                );
            }
        }

        user.setRole(role);

        userRepository.save(user);

        return "Cập nhật quyền thành công";
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(Integer userId) {

        User targetUser = getTargetUser(userId);

        userRepository.delete(targetUser);

        return "Xóa người dùng thành công";
    }

    /**
     * Helper method
     */
    private User getTargetUser(Integer userId) {

        User targetUser = userRepository.getUserById(userId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        User actor = userRepository.findUserByUsername(currentUser.getUsername()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ADMIN"));

        /**
         * ADMIN không được thao tác ADMIN khác
         */
        if (actor.getRole() == Role.ADMIN && targetUser.getRole() == Role.ADMIN && !actor.getId().equals(targetUser.getId())) {

            throw new AccessDeniedExceptionCustom("ADMIN không thể thao tác với ADMIN khác");
        }

        return targetUser;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(UserCreateDTO req) {

        if (userRepository.existsByUsername(req.getUsername())) {

            throw new DuplicateResourceException("Tên đăng nhập đã tồn tại");
        }

        if (userRepository.existsByEmail(req.getEmail())) {

            throw new DuplicateResourceException("Email đã tồn tại");
        }

        User user = new User();

        user.setUsername(req.getUsername());

        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        user.setFullName(req.getFullName());

        user.setEmail(req.getEmail());

        user.setRole(req.getRole());

        user.setIsActive(true);

        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }
}