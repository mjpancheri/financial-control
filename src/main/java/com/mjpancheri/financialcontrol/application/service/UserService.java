package com.mjpancheri.financialcontrol.application.service;

import com.mjpancheri.financialcontrol.application.exception.EmailDuplicateException;
import com.mjpancheri.financialcontrol.application.exception.ResourceNotFoundException;
import com.mjpancheri.financialcontrol.application.exception.UserNotFoundException;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.domain.user.UserRole;
import com.mjpancheri.financialcontrol.domain.user.dto.RegisterDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.UpdatePasswordDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.UpdateUserDTO;
import com.mjpancheri.financialcontrol.infrastructure.external.EmailService;
import com.mjpancheri.financialcontrol.infrastructure.persistence.UserRepository;
import com.mjpancheri.financialcontrol.infrastructure.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final EmailService emailService;

    public User getUserByAuthorizationToken(String authorizationToken) {
        String token = authorizationToken.replace("Bearer ", "");
        String email = tokenService.validateToken(token);

        return (User) userRepository.findByEmail(email);
    }

    public User getUser(String id) {
        var user = userRepository.findById(UUID.fromString(id));
        if (user.isEmpty()) {
            log.error("Error getUser with: " + id);
            throw new ResourceNotFoundException();
        }
        return user.get();
    }

    public User createUser(RegisterDTO data) {
        if (userRepository.findByEmail(data.email()) != null) {
            log.error("Error createUser with: " + data);
            throw new EmailDuplicateException(data.email());
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), encryptedPassword, UserRole.BASIC);

        return userRepository.save(newUser);
    }

    public void resetPassword(String email) {
        User user = (User) userRepository.findByEmail(email);
        if (user == null) {
            log.error("Error resetPassword with: " + email);
            throw new UserNotFoundException(email);
        }
        String token = tokenService.generateResetToken(user);
        //enviar email
        emailService.send(user.getEmail(), "Reset password", token);
    }

    public void updatePassword(UpdatePasswordDTO data) {
        String email = tokenService.validateResetToken(data.token());

        if (email.isEmpty()) {
            log.error("Error updatePassword with: " + data);
            throw new UserNotFoundException(email);
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = (User) userRepository.findByEmail(email);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }

    public User updateUser(String id, UpdateUserDTO data) {
        var foundUser = userRepository.findById(UUID.fromString(id));

        if (foundUser.isEmpty()) {
            log.error("Error updateUser with: " + id + " | " + data);
            throw new ResourceNotFoundException();
        }

        User user = foundUser.get();
        user.setName(data.name());
        user.setEmail(data.email());
        user.setRole(data.role());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        var foundUser = userRepository.findById(UUID.fromString(id));

        if (foundUser.isEmpty()) {
            log.error("Error deleteUser with: " + id);
            throw new ResourceNotFoundException();
        }

        User user = foundUser.get();
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public User restoreUser(String id) {
        var foundUser = userRepository.findById(UUID.fromString(id));

        if (foundUser.isEmpty()) {
            log.error("Error restoreUser with: " + id);
            throw new ResourceNotFoundException();
        }
        User user = foundUser.get();
        user.setDeletedAt(null);

        return userRepository.save(user);
    }
}
