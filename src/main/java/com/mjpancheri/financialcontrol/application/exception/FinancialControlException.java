package com.mjpancheri.financialcontrol.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class FinancialControlException extends RuntimeException {
    private final String code;
    private final transient Object[] args;
    private final HttpStatus status;

    public FinancialControlException(String code) {
        this.code = code;
        this.args = new Object[0];
        this.status = HttpStatus.BAD_REQUEST;
    }
}
