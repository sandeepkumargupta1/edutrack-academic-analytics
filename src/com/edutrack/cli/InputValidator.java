package com.edutrack.cli;

import java.util.regex.Pattern;

/**
 * Utility enforcing strict input validation rules on user console inputs.
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private static final Pattern REG_NO_PATTERN =
            Pattern.compile("^[0-9]{2}[A-Za-z]{3}[0-9]{4}$");

    public static boolean isValidRegNumber(String regNo) {
        if (regNo == null) return false;
        return REG_NO_PATTERN.matcher(regNo.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isDoubleInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    public static boolean isIntInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
}
