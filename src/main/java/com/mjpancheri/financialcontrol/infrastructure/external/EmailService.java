package com.mjpancheri.financialcontrol.infrastructure.external;

public interface EmailService {
    public void send(String to, String subject, String message);
}
