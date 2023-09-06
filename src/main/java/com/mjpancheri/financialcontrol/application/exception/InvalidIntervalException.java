package com.mjpancheri.financialcontrol.application.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InvalidIntervalException extends FinancialControlException {
    public InvalidIntervalException(LocalDate begin, LocalDate end) {
        super("error.income.summary.interval.message",
                new String[]{begin.format(DateTimeFormatter.BASIC_ISO_DATE),
                        end.format(DateTimeFormatter.BASIC_ISO_DATE)},
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public InvalidIntervalException(String month) {
        super("error.income.summary.month.message",
                new String[]{month},
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
