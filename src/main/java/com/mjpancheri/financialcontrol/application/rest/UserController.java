package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.application.service.UserService;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.domain.user.dto.UpdateUserDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable("id") @Valid String id) {
        var user = userService.getUser(id);

        return ResponseEntity.ok(user.convertTo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") @Valid String id,
                                                      @RequestBody @Valid UpdateUserDTO body) {
        User user = userService.updateUser(id, body);

        return ResponseEntity.ok(user.convertTo());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") @Valid String id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<UserResponseDTO>restoreUser(@PathVariable("id") @Valid String id) {
        User user = userService.restoreUser(id);

        return ResponseEntity.ok(user.convertTo());
    }
}
