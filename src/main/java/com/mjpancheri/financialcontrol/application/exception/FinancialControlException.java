package com.mjpancheri.financialcontrol.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class FinancialControlException extends RuntimeException {
    private final String message;
    private final String[] args;
    private final HttpStatus status;

    public FinancialControlException(String message) {
        this.message = message;
        this.args = new String[0];
        this.status = HttpStatus.BAD_REQUEST;
    }
}
