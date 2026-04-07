package com.cts.user_service.service;

import com.cts.user_service.dto.UserPublicDTO;
import com.cts.user_service.dto.UserUpdateDTO;
import com.cts.user_service.entity.User;
import com.cts.user_service.exception.CustomException;
import com.cts.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserPublicDTO loginUser(String email, String password) {
        log.info("Login attempt for user: {}", email);

        Optional<User> userOpt = userRepository.findByUserEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // ⚠️ No password check here since security is removed
            log.info("User found with role: {}", user.getUserRole().name());
            return toDTO(user);
        }

        log.warn("Login failed: User not found for email {}", email);
        throw new CustomException("Login Failed: User not found");
    }

    @Override
    public User registerUser(User user) {
        log.info("Registering new user with email: {}", user.getUserEmail());

        if (userRepository.findByUserEmail(user.getUserEmail()).isPresent()) {
            log.warn("Registration failed: Email {} already exists", user.getUserEmail());
            throw new CustomException("User with this email already exists");
        }

        // ⚠️ No password encoding since security is removed
        User savedUser = userRepository.save(user);
        log.info("User successfully registered with ID: {}", savedUser.getUserId());
        return savedUser;
    }

    @Override
    public User createUser(User user) {
        return registerUser(user);
    }

    @Override
    public UserPublicDTO updateUser(Long userId, UserUpdateDTO dto) {
        log.info("Updating user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with id: " + userId));

        if (dto.getUserName() != null) user.setUserName(dto.getUserName());
        if (dto.getUserEmail() != null) user.setUserEmail(dto.getUserEmail());
        if (dto.getUserRole() != null) user.setUserRole(dto.getUserRole());

        userRepository.save(user);
        log.info("User ID {} successfully updated", userId);
        return toDTO(user);
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Deleting user ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            throw new CustomException("User not found");
        }
        userRepository.deleteById(userId);
        log.info("User ID {} deleted successfully", userId);
    }

    @Override
    public UserPublicDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::toDTO)
                .orElseThrow(() -> new CustomException("Not found"));
    }

    @Override
    public List<UserPublicDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserPublicDTO getUserByEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .map(this::toDTO)
                .orElseThrow(() -> new CustomException("Not found"));
    }

    @Override
    public UserPublicDTO getUserByName(String userName) {
        return userRepository.findByUserName(userName)
                .map(this::toDTO)
                .orElseThrow(() -> new CustomException("Not found"));
    }

    private UserPublicDTO toDTO(User user) {
        UserPublicDTO dto = new UserPublicDTO();
        dto.setUserName(user.getUserName());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserContact(user.getUserContact());
        dto.setUserStatus(user.getUserStatus());
        dto.setUserRole(user.getUserRole().name());
        return dto;
    }
}
