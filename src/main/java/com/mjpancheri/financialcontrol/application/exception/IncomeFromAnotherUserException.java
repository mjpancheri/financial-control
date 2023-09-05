package com.mjpancheri.financialcontrol.application.exception;

public class IncomeFromAnotherUserException extends FinancialControlException {
    public IncomeFromAnotherUserException() {
        super("error.income.from.another.user.message");
    }
}
