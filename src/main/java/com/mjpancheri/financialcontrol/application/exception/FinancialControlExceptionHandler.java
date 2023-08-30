package com.mjpancheri.financialcontrol.application.exception;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Locale;

@AllArgsConstructor
@RestControllerAdvice
public class FinancialControlExceptionHandler {

    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleUnauthorizedException(ResourceNotFoundException exception) {
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                renderMessage("error.not.found.message", "teste"),
                null);

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleUnauthorizedException(UnauthorizedException exception) {
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                renderMessage("error.unauthorized.message"),
                null);

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleValidationRequestException(
            MethodArgumentNotValidException exception) {

        ArrayList<FieldErrorResponse> nestedErrors = new ArrayList<>();
        exception.getFieldErrors()
                .forEach(err -> {
                    FieldErrorResponse nestedError = new FieldErrorResponse(err.getDefaultMessage(), err.getField());
                    nestedErrors.add(nestedError);
                });
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                renderMessage("error.validation.message", exception.getObjectName()),
                nestedErrors);

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String renderMessage(String code) throws NoSuchMessageException {
        return renderMessage(code, null);
    }

    private String renderMessage(String code, Object... args) throws NoSuchMessageException {
        try {
            return messageSource.getMessage(code, args, Locale.getDefault());
        } catch (NoSuchMessageException exception) {
            return code;
        }
    }
}
