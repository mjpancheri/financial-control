package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.application.service.UserService;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.domain.user.dto.UpdateUserDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.UserResponseDTO;
import com.mjpancheri.financialcontrol.infrastructure.persistence.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") @Valid String id) {
        var user = userService.getUser(id);

        return ResponseEntity.ok(user.convertTo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") @Valid String id,
                                                      @RequestBody @Valid UpdateUserDTO body) {
        var foundUser = userRepository.findById(UUID.fromString(id));

        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = foundUser.get();
        user.setName(body.name());
        user.setEmail(body.email());
        user.setRole(body.role());
        userRepository.save(user);

        return ResponseEntity.ok(user.convertTo());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") @Valid String id) {
        var foundUser = userRepository.findById(UUID.fromString(id));

        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = foundUser.get();
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<UserResponseDTO>restoreUser(@PathVariable("id") @Valid String id) {
        var foundUser = userRepository.findById(UUID.fromString(id));

        if (foundUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = foundUser.get();
        user.setDeletedAt(null);
        userRepository.save(user);

        return ResponseEntity.ok(user.convertTo());
    }
}
