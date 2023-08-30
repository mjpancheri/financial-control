package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.application.service.I18nService;
import com.mjpancheri.financialcontrol.application.service.UserService;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.domain.user.dto.AuthenticationDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.LoginResponseDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.PasswordResponseDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.RegisterDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.ResetPasswordDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.UpdatePasswordDTO;
import com.mjpancheri.financialcontrol.domain.user.dto.UserResponseDTO;
import com.mjpancheri.financialcontrol.infrastructure.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;
    private final I18nService i18nService;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO body) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(body.email(), body.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterDTO body) throws URISyntaxException {
        User newUser = userService.createUser(body);

        return ResponseEntity.created(new URI("http://localhost:8080/users/"+newUser.getId())).body(newUser.convertTo());
    }

    @GetMapping("me")
    public ResponseEntity<UserResponseDTO> me(@RequestHeader(name = "Authorization") String authHeader) {
        User user = userService.getUserByAuthorizationToken(authHeader);

        return ResponseEntity.ok().body(user.convertTo());
    }

    @PostMapping("reset")
    public ResponseEntity<PasswordResponseDTO> resetPassword(@RequestBody ResetPasswordDTO body) {
        userService.resetPassword(body.email());

        return ResponseEntity.ok().body(new PasswordResponseDTO(i18nService.renderMessage("password.reset.message")));
    }

    @PostMapping("update")
    public ResponseEntity<PasswordResponseDTO> updatePassword(@RequestBody UpdatePasswordDTO body) {
        userService.updatePassword(body);

        return ResponseEntity.ok().body(new PasswordResponseDTO(i18nService.renderMessage("password.update.message")));
    }
}
