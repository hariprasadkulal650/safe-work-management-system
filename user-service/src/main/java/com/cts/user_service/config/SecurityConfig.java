package com.cts.user_service.config;

import com.cts.user_service.filter.JwtFilter;
import com.cts.user_service.filter.RoleAuthorizationInterceptor;
import com.cts.user_service.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security Configuration for User Service with Role-Based Access Control
 * 
 * Features:
 * - JWT token validation
 * - Role-based authorization
 * - Public endpoints: /users/register, /users/login
 * - Protected endpoints: /users/create (ADMIN), /users/update (ADMIN), etc.
 */
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

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
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - no authentication required
                        .requestMatchers("/users/register", "/users/login").permitAll()
                        .requestMatchers("/users/validate-token", "/users/refresh-token").permitAll()

                        // Protected endpoints - authentication required
                        .requestMatchers("/users/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(
                        new JwtFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    /**
     * Register role-based authorization interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RoleAuthorizationInterceptor())
                .addPathPatterns("/users/**")
                .excludePathPatterns("/users/register", "/users/login");
    }
}
