package com.mjpancheri.financialcontrol.application.exception;

public class ResourceNotFoundException extends FinancialControlException {
    public ResourceNotFoundException() {
        super("error.not.found.message");
    }
}
