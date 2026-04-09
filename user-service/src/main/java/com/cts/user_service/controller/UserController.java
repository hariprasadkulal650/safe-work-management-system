package com.cts.user_service.controller;

import com.cts.user_service.annotation.RequiredRole;
import com.cts.user_service.dto.LoginRequestDTO;
import com.cts.user_service.dto.UserPublicDTO;
import com.cts.user_service.dto.UserUpdateDTO;
import com.cts.user_service.entity.User;
import com.cts.user_service.enums.UserRole;
import com.cts.user_service.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register") // Creating User
    public ResponseEntity<User> register(@RequestBody User user) {
        log.info("Request to register user: {}", user.getUserEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(user));
    }

    @PostMapping("/login") // User Login
    public ResponseEntity<UserPublicDTO> login(@RequestBody LoginRequestDTO request) {
        log.info("Request to login user: {}", request.getEmail());
        return ResponseEntity.ok(userService.loginUser(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/create") // Admin creating new users
    @RequiredRole(UserRole.ADMIN)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.info("Request to create user via admin: {}", user.getUserEmail());
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PatchMapping("/updateUser/{userId}") // Updating changes in attribute
    @RequiredRole(UserRole.ADMIN)
    public ResponseEntity<UserPublicDTO> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateDTO dto) {
        log.info("Request to update user ID: {}", userId);
        return ResponseEntity.ok(userService.updateUser(userId, dto));
    }

    @DeleteMapping("/delete/{userId}") // Deleting user
    @RequiredRole(UserRole.ADMIN)
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        log.info("Request to delete user ID: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/getUserById/{userId}") // Display data using userid
    @RequiredRole({UserRole.ADMIN, UserRole.COMPLIANCE_OFFICER, UserRole.SAFETY_OFFICER})
    public ResponseEntity<UserPublicDTO> getUserById(@PathVariable Long userId) {
        log.info("Fetching user by ID: {}", userId);
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/getAllUsers") // Display all users
    @RequiredRole({UserRole.ADMIN, UserRole.COMPLIANCE_OFFICER})
    public ResponseEntity<List<UserPublicDTO>> getAllUsers() {
        log.info("Fetching all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/getByEmail/{userEmail}") //Display users by email
    @RequiredRole({UserRole.ADMIN, UserRole.COMPLIANCE_OFFICER, UserRole.SAFETY_OFFICER})
    public ResponseEntity<UserPublicDTO> getUserByEmail(@PathVariable String userEmail) {
        log.info("Fetching user by email: {}", userEmail);
        return ResponseEntity.ok(userService.getUserByEmail(userEmail));
    }

    @GetMapping("/getByName/{userName}")
    @RequiredRole({UserRole.ADMIN, UserRole.COMPLIANCE_OFFICER, UserRole.SAFETY_OFFICER})
    public ResponseEntity<UserPublicDTO> getUserByName(@PathVariable String userName) {
        log.info("Fetching user by name: {}", userName);
        return ResponseEntity.ok(userService.getUserByName(userName));
    }
}