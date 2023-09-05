package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.application.service.UserService;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.domain.user.dto.UpdateUserDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService service;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") @Valid String id) {
        var user = service.getUser(id);

        return ResponseEntity.ok(user.convertTo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") @UUID String id,
                                                      @RequestBody @Valid UpdateUserDTO body) {
        User user = service.updateUser(id, body);

        return ResponseEntity.ok(user.convertTo());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") @Valid String id) {
        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<UserResponseDTO>restoreUser(@PathVariable("id") @Valid String id) {
        User user = service.restoreUser(id);

        return ResponseEntity.ok(user.convertTo());
    }
}
