package re.edu.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import re.edu.security.jwt.JwtFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // disable csrf
                .csrf(AbstractHttpConfigurer::disable)

                // stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // ================= PUBLIC =================
                        .requestMatchers("/api/auth/login", "/api/auth/verify").permitAll()

                        // ================= AUTH =================
                        .requestMatchers("/api/auth/me").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        // ================= USERS (ADMIN ONLY) =================
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // ================= STUDENTS =================
                        .requestMatchers("/api/students/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        // ================= MENTORS =================
                        .requestMatchers("/api/mentors/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")

                        // ================= INTERNSHIP =================
                        .requestMatchers("/api/internship_phases/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        // ================= EVALUATION CRITERIA =================
                        .requestMatchers("/api/evaluation_criteria/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        // ================= ASSESSMENT ROUNDS =================
                        .requestMatchers("/api/assessment_rounds/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        // ================= round_criteria =================
                        .requestMatchers("/api/round_criteria/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        // ================= INTERNSHIP ASSIGNMENTS =================
                        .requestMatchers("/api/internship_assignments/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        // ================= ASSESSMENT RESULTS =================
                        .requestMatchers("/api/assessment_results/**").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        // fallback
                        .anyRequest().authenticated())

                // exception handling
                .exceptionHandling(ex -> ex.accessDeniedHandler(customAccessDeniedHandler))

                // jwt filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // disable default auth
                .httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}