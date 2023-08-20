package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.domain.user.UserRole;
import com.mjpancheri.financialcontrol.domain.user.dto.AuthenticationDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.LoginResponseDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.RegisterDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.UserResponseDTO;
import com.mjpancheri.financialcontrol.infrastructure.persistence.UserRepository;
import com.mjpancheri.financialcontrol.infrastructure.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    @PostMapping("/login")
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
}
