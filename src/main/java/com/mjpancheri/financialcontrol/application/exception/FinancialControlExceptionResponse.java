package com.mjpancheri.financialcontrol.application.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FinancialControlExceptionResponse(String message, List<FieldErrorResponse> nestedErrors, LocalDateTime date) {
}
