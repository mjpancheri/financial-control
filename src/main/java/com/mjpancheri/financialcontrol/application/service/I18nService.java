package com.mjpancheri.financialcontrol.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class I18nService {
    private final MessageSource messageSource;

    public String renderMessage(String code) throws NoSuchMessageException {
        return renderMessage(code, (Object) null);
    }

    public String renderMessage(String code, Object... args) throws NoSuchMessageException {
        try {
            return messageSource.getMessage(code, args, Locale.getDefault());
        } catch (NoSuchMessageException exception) {
            return code;
        }
    }
}
