package com.mjpancheri.financialcontrol.domain.summary.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncomeSummaryResponseDTO(UUID id, String title, String description, String type, String category,
                                       Double amount, LocalDate date, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
