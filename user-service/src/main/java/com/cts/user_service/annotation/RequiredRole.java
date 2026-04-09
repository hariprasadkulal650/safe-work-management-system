package com.cts.user_service.annotation;

import com.cts.user_service.enums.UserRole;
import java.lang.annotation.*;

/**
 * Annotation to specify required roles for accessing a method or controller.
 * Supports both single and multiple roles.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredRole {
    /**
     * Array of roles required to access the annotated resource
     * Any of these roles is sufficient for access
     */
    UserRole[] value() default {};

    /**
     * Whether all roles are required (AND logic) or any role is sufficient (OR logic)
     * Default: false (OR logic - any role is sufficient)
     */
    boolean requireAll() default false;
}
