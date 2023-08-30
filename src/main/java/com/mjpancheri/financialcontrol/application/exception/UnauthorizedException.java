package com.mjpancheri.financialcontrol.application.exception;

public class UnauthorizedException extends FinancialControlException {
    public UnauthorizedException() {
        super("error.unauthorized.message");
    }
}
