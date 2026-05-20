package re.edu.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import re.edu.exception.JwtExceptionCustom;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private static final List<String> PUBLIC_API = List.of(
            "/api/auth/login",
            "/api/auth/verify"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            String path = request.getRequestURI();

            // ===== DEBUG REQUEST =====
            System.out.println("================================");
            System.out.println("REQUEST URL: " + path);
            System.out.println("AUTH HEADER: " + request.getHeader("Authorization"));

            // skip public api
            if (PUBLIC_API.contains(path)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = getTokenFromRequest(request);

            System.out.println("TOKEN: " + token);

            if (token == null) {
                System.out.println("NO TOKEN -> SKIP AUTH");
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                // validate token
                jwtProvider.validateToken(token);

                // get username
                String username = jwtProvider.getUsernameFromToken(token);

                System.out.println("USERNAME FROM TOKEN: " + username);

                // load user
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                System.out.println("USER AUTHORITIES FROM DB: " + userDetails.getAuthorities());

                // create authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                // set authentication
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ===== DEBUG AFTER SET =====
                System.out.println("AFTER SET AUTH:");
                System.out.println(SecurityContextHolder.getContext().getAuthentication());
                System.out.println("AUTHORITIES IN CONTEXT: " +
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            }

            filterChain.doFilter(request, response);

        } catch (JwtExceptionCustom ex) {

            System.out.println("JWT ERROR: " + ex.getMessage());

            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    ex
            );
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || authorization.isBlank()) {
            return null;
        }

        if (!authorization.startsWith("Bearer ")) {
            throw new JwtExceptionCustom("Authorization header không hợp lệ");
        }

        String token = authorization.substring(7).trim();

        if (token.isBlank()) {
            throw new JwtExceptionCustom("Token rỗng hoặc null");
        }

        return token;
    }
}