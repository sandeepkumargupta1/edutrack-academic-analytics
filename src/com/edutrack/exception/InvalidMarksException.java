package com.edutrack.exception;

/**
 * Thrown when academic assessment mark inputs (Internal Tests, Lab, Assignment, Quiz)
 * fall outside permitted boundary ranges.
 */
public class InvalidMarksException extends InvalidAcademicRecordException {
    private static final long serialVersionUID = 1L;

    public InvalidMarksException(String message) {
        super(message);
    }
}
