package com.mjpancheri.financialcontrol.domain.summary;

import lombok.Getter;

@Getter
public enum IncomeType {

    REVENUE("revenue"),
    EXPENSE("expense");

    private final String type;

    IncomeType(String type) {
        this.type = type;
    }
}
