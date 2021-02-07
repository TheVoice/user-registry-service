package com.karol.users.utils;

import java.time.LocalDate;
import java.time.Period;

public class DateValidator {

    public static boolean isAdultWithDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        } else {
            LocalDate now = LocalDate.now();
            if (Period.between(dateOfBirth, now).getYears() >= 18) {
                return true;
            } else {
                return false;
            }
        }
    }
}
