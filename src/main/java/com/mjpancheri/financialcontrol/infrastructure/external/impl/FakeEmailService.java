package com.mjpancheri.financialcontrol.infrastructure.external.impl;

import com.mjpancheri.financialcontrol.infrastructure.external.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FakeEmailService implements EmailService {
    @Override
    public void send(String to, String subject, String message) {
        log.info("""
                        
                        ============== Send Email - start ===============
                        TO: {}
                        SUBJECT: {}
                        MESSAGE: {}
                        ============== Send Email - end   ===============""", to, subject, message);
    }
}
