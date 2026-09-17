package com.edutrack.exception;

/**
 * Thrown when student demographic profile data (registration number, name, email, semester, CGPA)
 * violates domain invariants or format constraints.
 */
public class InvalidStudentDataException extends EduTrackException {
    private static final long serialVersionUID = 1L;

    public InvalidStudentDataException(String message) {
        super(message);
    }

    public InvalidStudentDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
