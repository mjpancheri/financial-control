package com.mjpancheri.financialcontrol.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    BASIC("basic");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

}
