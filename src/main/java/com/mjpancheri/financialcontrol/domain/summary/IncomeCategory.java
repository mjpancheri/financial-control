package com.mjpancheri.financialcontrol.domain.summary;

import lombok.Getter;

@Getter
public enum IncomeCategory {
    BASIC("basic"),
    ENTERTAINMENT("entertainment"),
    FOOD("food"),
    GIFT("gift"),
    HEALTH("health"),
    LOAN("loan"),
    NEEDLESS("needless"),
    OTHER("other"),
    REPAIR("repair");

    private final String category;

    IncomeCategory(String category) {
        this.category = category;
    }

}
