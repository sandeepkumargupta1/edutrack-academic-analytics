package com.edutrack.exception;

/**
 * Base custom exception for the EduTrack application.
 */
public class EduTrackException extends Exception {
    private static final long serialVersionUID = 1L;

    public EduTrackException(String message) {
        super(message);
    }

    public EduTrackException(String message, Throwable cause) {
        super(message, cause);
    }
}
