package com.edutrack.model;

import java.util.Arrays;
import java.util.List;

/**
 * Concrete implementation representing an enrolled student account.
 */
public class StudentUser extends User {
    private static final long serialVersionUID = 1L;

    private final String registrationNumber;

    public StudentUser(String username, String passwordHash, String fullName, String email, String registrationNumber) {
        super(username, passwordHash, fullName, email, UserRole.STUDENT);
        if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        this.registrationNumber = registrationNumber.trim().toUpperCase();
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public List<String> getDashboardCapabilities() {
        return Arrays.asList(
            "VIEW_SELF_ACADEMICS",
            "VIEW_SELF_ATTENDANCE",
            "VIEW_SELF_INTERVENTIONS"
        );
    }
}
