package com.mjpancheri.financialcontrol.domain.summary.dto;

import com.mjpancheri.financialcontrol.domain.summary.IncomeCategory;
import com.mjpancheri.financialcontrol.domain.summary.IncomeType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateIncomeSummaryDTO(String title, String description, IncomeType type, IncomeCategory category, BigDecimal amount, LocalDate date) {
}
