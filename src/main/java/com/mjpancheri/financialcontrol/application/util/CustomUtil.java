package com.mjpancheri.financialcontrol.application.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjpancheri.financialcontrol.application.exception.FinancialControlException;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class CustomUtil {

    public static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new FinancialControlException("Unexpected error: " + e.getMessage());
        }
    }

    public static MessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
