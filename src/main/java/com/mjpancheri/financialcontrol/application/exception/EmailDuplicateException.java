package com.mjpancheri.financialcontrol.application.exception;

import org.springframework.http.HttpStatus;

public class EmailDuplicateException extends FinancialControlException {
    public EmailDuplicateException(String email) {
        super("error.email.duplicate.message", new String[]{email}, HttpStatus.CONFLICT);
    }
}
