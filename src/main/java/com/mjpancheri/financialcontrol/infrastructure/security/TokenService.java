package com.mjpancheri.financialcontrol.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mjpancheri.financialcontrol.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    @Value("${api.security.token.timeout}")
    private Long timeout;
    @Value("${api.security.reset.token.secret}")
    private String resetSecret;
    @Value("${api.security.reset.token.timeout}")
    private Long resetTimeout;
    @Value("${api.name}")
    private String applicationName;
    @Value("${api.timezone}")
    private String timezone;

    public String generateToken(User user) {
        try {
            Instant now = LocalDateTime.now().toInstant(ZoneOffset.of(timezone));
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(applicationName)
                    .withIssuedAt(now)
                    .withSubject(user.getEmail())
                    .withExpiresAt(now.plusMillis(timeout))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    /**
     * Validate token and return the subject (email)
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(applicationName)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    public String generateResetToken(User user) {
        try {
            Instant now = LocalDateTime.now().toInstant(ZoneOffset.of(timezone));
            Algorithm algorithm = Algorithm.HMAC256(resetSecret);
            return JWT.create()
                    .withIssuer(applicationName)
                    .withIssuedAt(now)
                    .withSubject(user.getEmail())
                    .withExpiresAt(now.plusMillis(resetTimeout))
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating reset token", exception);
        }
    }

    /**
     * Validate token and return the subject (email)
     */
    public String validateResetToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(resetSecret);
            return JWT.require(algorithm)
                    .withIssuer(applicationName)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }
}
