package com.edutrack.cli;

import com.edutrack.exception.InvalidMarksException;
import com.edutrack.exception.InvalidStudentDataException;

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

    /**
     * Validates student demographic fields and throws InvalidStudentDataException if invalid.
     */
    public static void validateStudentProfile(String regNo, String name, String email, int semester, double cgpa)
            throws InvalidStudentDataException {
        if (!isValidRegNumber(regNo)) {
            throw new InvalidStudentDataException("Invalid registration number format: " + regNo + ". Expected e.g. 23BCE1001.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidStudentDataException("Student name cannot be empty.");
        }
        if (!isValidEmail(email)) {
            throw new InvalidStudentDataException("Invalid email format: " + email + ". Expected e.g. student@vitstudent.ac.in.");
        }
        if (semester < 1 || semester > 10) {
            throw new InvalidStudentDataException("Semester must be between 1 and 10. Received: " + semester);
        }
        if (cgpa < 0.0 || cgpa > 10.0) {
            throw new InvalidStudentDataException("CGPA must be between 0.0 and 10.0. Received: " + cgpa);
        }
    }

    /**
     * Validates continuous assessment marks and throws InvalidMarksException if invalid.
     */
    public static void validateMarks(double it1, double it2, double asgn, double lab, double quiz)
            throws InvalidMarksException {
        if (it1 < 0.0 || it1 > 50.0) {
            throw new InvalidMarksException("Internal Test 1 mark must be between 0.0 and 50.0. Received: " + it1);
        }
        if (it2 < 0.0 || it2 > 50.0) {
            throw new InvalidMarksException("Internal Test 2 mark must be between 0.0 and 50.0. Received: " + it2);
        }
        if (asgn < 0.0 || asgn > 20.0) {
            throw new InvalidMarksException("Assignment mark must be between 0.0 and 20.0. Received: " + asgn);
        }
        if (lab < 0.0 || lab > 30.0) {
            throw new InvalidMarksException("Lab score must be between 0.0 and 30.0. Received: " + lab);
        }
        if (quiz < 0.0 || quiz > 20.0) {
            throw new InvalidMarksException("Quiz score must be between 0.0 and 20.0. Received: " + quiz);
        }
    }
}

