package com.cts.security_service.service;

import com.cts.security_service.client.EmployeeClient;
import com.cts.security_service.client.UserClient;
import com.cts.security_service.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeClient employeeClient;
    private final UserClient userClient;

    public CustomUserDetailsService(EmployeeClient employeeClient, UserClient userClient) {
        this.employeeClient = employeeClient;
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            // Try to find employee first
            var employee = employeeClient.findByEmail(email);
            if (employee != null) {
                return new org.springframework.security.core.userdetails.User(
                        email,
                        employee.getPassword(),
                        new ArrayList<>()  // Employees have no roles
                );
            }
        } catch (Exception ignored) {}

        try {
            // Try to find user with roles
            User user = userClient.findByEmail(email);
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            
            if (user.getRoles() != null) {
                authorities = user.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUserEmail(),
                    user.getPassword(),
                    authorities
            );
        } catch (Exception e) {
            throw new UsernameNotFoundException("No employee or user found with email: " + email);
        }
    }
}