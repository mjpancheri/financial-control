package com.mjpancheri.financialcontrol.application.service;

import com.mjpancheri.financialcontrol.application.exception.ResourceNotFoundException;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.infrastructure.persistence.UserRepository;
import com.mjpancheri.financialcontrol.infrastructure.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public User getUserByAuthorizationToken(String authorizationToken) throws AuthenticationException {
        String token = authorizationToken.replace("Bearer ", "");
        String email = tokenService.validateToken(token);

        return (User) userRepository.findByEmail(email);
    }

    public User getUser(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(ResourceNotFoundException::new);
    }
}
