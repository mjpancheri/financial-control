package com.mjpancheri.financialcontrol.application.util;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    private DateUtil() {
    }

    public static boolean isYearMonthPatterCorrect(String month) {
        if (month == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}");
        Matcher matcher = pattern.matcher(month);

        return matcher.matches()
                && Integer.parseInt(month.substring(5)) > 0
                && Integer.parseInt(month.substring(5)) < 13;
    }

    public static LocalDate getMonthFirstDay(String month) {
        if (!isYearMonthPatterCorrect(month)) {
            return null;
        }

        return LocalDate.parse(month + "-01");
    }

    public static LocalDate getMonthLastDay(String month) {
        if (!isYearMonthPatterCorrect(month)) {
            return null;
        }

        LocalDate date = getMonthFirstDay(month);
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }
}
