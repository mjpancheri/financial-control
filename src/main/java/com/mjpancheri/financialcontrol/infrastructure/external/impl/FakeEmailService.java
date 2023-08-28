package com.mjpancheri.financialcontrol.infrastructure.external.impl;

import com.mjpancheri.financialcontrol.infrastructure.external.EmailService;
import org.springframework.stereotype.Service;

@Service
public class FakeEmailService implements EmailService {
    @Override
    public void send(String to, String subject, String message) {
        String log = """
                        ============== Send Email - start ===============
                        TO: %s
                        SUBJECT: %s
                        MESSAGE: %s
                        ============== Send Email - end   ===============""".formatted(to, subject, message);
        System.out.println(log);
    }
}
