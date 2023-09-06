package com.mjpancheri.financialcontrol.application.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DateUtilTest {

    @Test
    void testIsYearMonthPatterCorrect() {
        assertTrue(DateUtil.isYearMonthPatterCorrect("2023-02"));
        assertTrue(DateUtil.isYearMonthPatterCorrect("2023-11"));
        assertFalse(DateUtil.isYearMonthPatterCorrect("2023-00"));
        assertFalse(DateUtil.isYearMonthPatterCorrect("2023-13"));
        assertFalse(DateUtil.isYearMonthPatterCorrect("wrong"));
        assertFalse(DateUtil.isYearMonthPatterCorrect(null));
    }

    @Test
    void testGetMonthFirstDay() {
        LocalDate date = LocalDate.of(2023, Month.SEPTEMBER, 1);

        assertEquals(date, DateUtil.getMonthFirstDay("2023-09"));
    }

    @Test
    void testGetMonthLastDay() {
        assertEquals(LocalDate.of(2023, Month.FEBRUARY, 28), DateUtil.getMonthLastDay("2023-02"));
        assertEquals(LocalDate.of(2023, Month.MARCH, 31), DateUtil.getMonthLastDay("2023-03"));
        assertEquals(LocalDate.of(2023, Month.SEPTEMBER, 30), DateUtil.getMonthLastDay("2023-09"));
        assertEquals(LocalDate.of(2024, Month.FEBRUARY, 29), DateUtil.getMonthLastDay("2024-02"));
    }
}