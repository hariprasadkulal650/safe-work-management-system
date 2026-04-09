package com.cts.user_service.filter;

import com.cts.user_service.annotation.RequiredRole;
import com.cts.user_service.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Role-based Authorization Interceptor for User Service
 * Checks if user has required roles to access the endpoint
 */
public class RoleAuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // Check method-level annotation first, then class-level
        RequiredRole requiredRole = handlerMethod.getMethodAnnotation(RequiredRole.class);
        if (requiredRole == null) {
            requiredRole = handlerMethod.getBeanType().getAnnotation(RequiredRole.class);
        }

        // If no annotation, allow access
        if (requiredRole == null) {
            return true;
        }

        // Get current authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized: User not authenticated\"}");
            return false;
        }

        // Get user roles
        List<String> userRoles = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList();

        // Get required roles
        List<String> requiredRoles = Arrays.stream(requiredRole.value())
                .map(UserRole::name)
                .toList();

        // Check authorization
        boolean hasAccess;
        if (requiredRole.requireAll()) {
            // User must have ALL required roles
            hasAccess = userRoles.containsAll(requiredRoles);
        } else {
            // User must have at least ONE required role
            hasAccess = userRoles.stream()
                    .anyMatch(requiredRoles::contains);
        }

        if (!hasAccess) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Forbidden: User does not have required roles\"}");
            return false;
        }

        return true;
    }
}
