package com.mjpancheri.financialcontrol.application.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

@AllArgsConstructor
@RestControllerAdvice
public class FinancialControlExceptionHandler {

    private MessageSource messageSource;

    private String renderMessage(String code, Object... args) throws NoSuchMessageException {
        try {
            return messageSource.getMessage(code, args, Locale.getDefault());
        } catch (NoSuchMessageException exception) {
            return code;
        }
    }

    @ExceptionHandler(FinancialControlException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleFinancialControlException(FinancialControlException exception) {
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                renderMessage(exception.getCode(), exception.getArgs()),
                null, LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), exception.getStatus());
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleInternalServerError(HttpServerErrorException.InternalServerError exception) {
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                renderMessage("error.internal.server.message", exception.getMessage()),
                null, LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleUnauthorizedException(UnauthorizedException exception) {
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                renderMessage(exception.getCode(), exception.getArgs()),
                null, LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), exception.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleMethodArgumentNotValidExceptionException(
            MethodArgumentNotValidException exception) {

        ArrayList<FieldErrorResponse> nestedErrors = new ArrayList<>();
        exception.getFieldErrors()
                .forEach(err -> {
                    FieldErrorResponse nestedError = new FieldErrorResponse(err.getDefaultMessage(), err.getField());
                    nestedErrors.add(nestedError);
                });
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                renderMessage("error.validation.argument.message", exception.getObjectName()),
                nestedErrors, LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleConstraintViolationExceptionException(
            ConstraintViolationException exception) {

        ArrayList<FieldErrorResponse> nestedErrors = new ArrayList<>();
        exception.getConstraintViolations()
                .forEach(err -> {
                    FieldErrorResponse nestedError = new FieldErrorResponse(err.getPropertyPath().toString(), err.getMessage());
                    nestedErrors.add(nestedError);
                });
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                renderMessage("error.validation.constraint.message", exception.getLocalizedMessage()),
                nestedErrors, LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
