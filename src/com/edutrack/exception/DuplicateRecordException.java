package com.edutrack.exception;

/**
 * Thrown when trying to insert a student with an already existing registration number.
 */
public class DuplicateRecordException extends EduTrackException {
    private static final long serialVersionUID = 1L;

    public DuplicateRecordException(String message) {
        super(message);
    }
}
