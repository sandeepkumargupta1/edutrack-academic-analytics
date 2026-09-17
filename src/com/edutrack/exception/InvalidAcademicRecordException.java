package com.edutrack.exception;

/**
 * Thrown when academic assessment scores or attendance metrics violate business rules.
 */
public class InvalidAcademicRecordException extends EduTrackException {
    private static final long serialVersionUID = 1L;

    public InvalidAcademicRecordException(String message) {
        super(message);
    }
}
