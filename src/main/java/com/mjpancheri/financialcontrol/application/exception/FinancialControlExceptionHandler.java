package com.mjpancheri.financialcontrol.application.exception;

import com.mjpancheri.financialcontrol.application.service.I18nService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestControllerAdvice
public class FinancialControlExceptionHandler {

    private final I18nService i18nService;

    @ExceptionHandler(FinancialControlException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleFinancialControlException(FinancialControlException exception) {
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                i18nService.renderMessage(exception.getCode(), exception.getArgs()),
                null, LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), exception.getStatus());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<FinancialControlExceptionResponse> handleUnauthorizedException(UnauthorizedException exception) {
        FinancialControlExceptionResponse res = new FinancialControlExceptionResponse(
                i18nService.renderMessage(exception.getCode(), exception.getArgs()),
                null, LocalDateTime.now());

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
                i18nService.renderMessage("error.validation.message", exception.getObjectName()),
                nestedErrors, LocalDateTime.now());

        return new ResponseEntity<>(res, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }


}
