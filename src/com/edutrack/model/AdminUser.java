package com.edutrack.model;

import java.util.Arrays;
import java.util.List;

/**
 * Concrete implementation representing a System Administrator.
 */
public class AdminUser extends User {
    private static final long serialVersionUID = 1L;

    public AdminUser(String username, String passwordHash, String fullName, String email) {
        super(username, passwordHash, fullName, email, UserRole.ADMIN);
    }

    @Override
    public List<String> getDashboardCapabilities() {
        return Arrays.asList(
            "MANAGE_USERS",
            "VIEW_STUDENTS",
            "UPDATE_MARKS",
            "RUN_ANALYTICS",
            "TRIGGER_INTERVENTIONS",
            "EXPORT_REPORTS",
            "SYSTEM_MAINTENANCE"
        );
    }
}
