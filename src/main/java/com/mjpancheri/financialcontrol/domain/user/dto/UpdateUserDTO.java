package com.mjpancheri.financialcontrol.domain.user.dto;

import com.mjpancheri.financialcontrol.domain.user.UserRole;

public record UpdateUserDTO(String name, String email, UserRole role) {
}
