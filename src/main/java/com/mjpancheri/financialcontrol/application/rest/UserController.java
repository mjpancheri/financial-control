package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.domain.user.dto.UserResponseDTO;
import com.mjpancheri.financialcontrol.infrastructure.persistence.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("users")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") @Valid String id) {
        var foundUser = userRepository.findById(UUID.fromString(id));
        return foundUser.map(user -> ResponseEntity.ok(user.convertTo()))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
