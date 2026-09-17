package com.edutrack.exception;

/**
 * Thrown when credentials fail or unauthorized actions are attempted.
 */
public class AuthenticationException extends EduTrackException {
    private static final long serialVersionUID = 1L;

    public AuthenticationException(String message) {
        super(message);
    }
}
