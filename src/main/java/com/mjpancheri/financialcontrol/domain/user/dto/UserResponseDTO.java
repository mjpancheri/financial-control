package com.mjpancheri.financialcontrol.domain.user.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(UUID id, String name, String email, String role, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
