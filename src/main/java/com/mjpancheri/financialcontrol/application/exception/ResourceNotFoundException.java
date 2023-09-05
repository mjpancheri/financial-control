package com.mjpancheri.financialcontrol.application.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends FinancialControlException {
    public ResourceNotFoundException() {
        super("error.not.found.message", HttpStatus.NOT_FOUND);
    }
}
