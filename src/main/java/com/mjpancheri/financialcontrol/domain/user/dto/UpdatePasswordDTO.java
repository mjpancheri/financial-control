package com.mjpancheri.financialcontrol.domain.user.dto;

import com.mjpancheri.financialcontrol.domain.user.UserRole;

public record UpdatePasswordDTO(String token, String password) {
}
