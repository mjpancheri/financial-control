package com.mjpancheri.financialcontrol.application.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends FinancialControlException {

    public UserNotFoundException(String email) {
        super("error.email.not.found.message", new String[]{email}, HttpStatus.NOT_FOUND);
    }
}
