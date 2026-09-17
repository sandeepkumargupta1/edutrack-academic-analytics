package com.edutrack.exception;

/**
 * Thrown when a student cannot be found by their registration number or query criteria.
 */
public class StudentNotFoundException extends EduTrackException {
    private static final long serialVersionUID = 1L;

    public StudentNotFoundException(String regNumber) {
        super("Student with Registration Number '" + regNumber + "' was not found.");
    }
}
