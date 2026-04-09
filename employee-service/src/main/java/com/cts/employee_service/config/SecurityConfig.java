package com.cts.employee_service.config;

import com.cts.employee_service.filter.JwtFilter;
import com.cts.employee_service.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security Configuration for Employee Service
 * 
 * Simple Authentication Model:
 * - JWT token validation only
 * - No role-based access control
 * - All authenticated employees have equal access
 * - Public endpoints: /employees/register, /employees/login
 * - Protected endpoints: All other /employees/** require valid JWT token
 */
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Password encoder bean for bcrypt password hashing
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security filter chain configuration
     * Simple authentication: validate JWT token, allow all authenticated users
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - no authentication required
                        .requestMatchers("/employees/register", "/employees/login").permitAll()
                        .requestMatchers("/actuator/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Protected endpoints - authentication required
                        // All authenticated employees have equal access (no role differentiation)
                        .requestMatchers("/employees/**").authenticated()
                        .anyRequest().permitAll()
                )
                // Add JWT filter for token validation
                .addFilterBefore(
                        new JwtFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}