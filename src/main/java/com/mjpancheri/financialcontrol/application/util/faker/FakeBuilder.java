package com.mjpancheri.financialcontrol.application.util.faker;

import com.mjpancheri.financialcontrol.domain.summary.IncomeCategory;
import com.mjpancheri.financialcontrol.domain.summary.IncomeSummary;
import com.mjpancheri.financialcontrol.domain.summary.IncomeType;
import com.mjpancheri.financialcontrol.domain.summary.dto.CreateIncomeSummaryDTO;
import com.mjpancheri.financialcontrol.domain.summary.dto.IncomeSummaryResponseDTO;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.domain.user.UserRole;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class FakeBuilder {

    private FakeBuilder(){}

    public static IncomeSummaryResponseDTO buildIncomeSummaryResponseDTO() {
        return new IncomeSummaryResponseDTO(UUID.randomUUID(),
                "Title",
                "Description",
                IncomeType.EXPENSE.getType(),
                IncomeCategory.OTHER.getCategory(),
                BigDecimal.TEN.doubleValue(),
                LocalDate.now().plusDays(-10),
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static CreateIncomeSummaryDTO buildCreateIncomeSummaryDTO() {
        return new CreateIncomeSummaryDTO("Title",
                "Description",
                IncomeType.EXPENSE,
                IncomeCategory.OTHER,
                BigDecimal.TEN,
                LocalDate.now().plusDays(-10));
    }

    public static IncomeSummary buildIncomeSummary(User user) {
        return new IncomeSummary(UUID.randomUUID(),
                user,
                "Title",
                "Description",
                IncomeType.EXPENSE,
                IncomeCategory.OTHER,
                BigDecimal.TEN,
                LocalDate.now().plusDays(-10),
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    public static User buildUser(String name) {
        return new User(UUID.randomUUID(),
                name,
                name.trim().replace(" ", ".").toLowerCase() + "@example.com",
                "$2a$10$l3v3Kdb6Ta.pfvi7qg3XB.FqlUqIlBq4si3Z2FWEMxI1wYPOyIBrS",
                UserRole.BASIC,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);
    }
}
