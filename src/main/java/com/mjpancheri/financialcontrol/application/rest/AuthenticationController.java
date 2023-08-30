package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.application.service.AuthorizationService;
import com.mjpancheri.financialcontrol.application.service.UserService;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.domain.user.UserRole;
import com.mjpancheri.financialcontrol.domain.user.dto.*;
import com.mjpancheri.financialcontrol.infrastructure.external.EmailService;
import com.mjpancheri.financialcontrol.infrastructure.persistence.UserRepository;
import com.mjpancheri.financialcontrol.infrastructure.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO body) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(body.email(), body.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterDTO body) throws URISyntaxException {
        if (userRepository.findByEmail(body.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(body.password());
        User newUser = new User(body.name(), body.email(), encryptedPassword, UserRole.BASIC);

        userRepository.save(newUser);

        return ResponseEntity.created(new URI("http://localhost:8080/users/"+newUser.getId())).body(newUser.convertTo());
    }

    @GetMapping("me")
    public ResponseEntity<UserResponseDTO> me(@RequestHeader(name = "Authorization") String authHeader)
            throws AuthenticationException {
        User user = userService.getUserByAuthorizationToken(authHeader);

        return ResponseEntity.ok().body(user.convertTo());
    }

    @PostMapping("reset")
    public ResponseEntity<PasswordResponseDTO> resetPassword(@RequestBody ResetPasswordDTO body) {
        User user = (User) userRepository.findByEmail(body.email());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        String token = tokenService.generateResetToken(user);
        //enviar email
        emailService.send(user.getEmail(), "Reset password", token);
        return ResponseEntity.ok().body(new PasswordResponseDTO("Email sent to reset password"));
    }

    @PostMapping("update")
    public ResponseEntity<PasswordResponseDTO> updatePassword(@RequestBody UpdatePasswordDTO body) {
        String email = tokenService.validateResetToken(body.token());

        if (email.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(body.password());
        User user = (User) userRepository.findByEmail(email);
        user.setPassword(encryptedPassword);
        userRepository.save(user);

        return ResponseEntity.ok().body(new PasswordResponseDTO("Password updated successfully"));
    }
}
