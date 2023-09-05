package com.mjpancheri.financialcontrol.application.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends FinancialControlException {
    public UnauthorizedException() {
        super("error.unauthorized.message", HttpStatus.UNAUTHORIZED);
    }
}
