package com.edutrack.model;

/**
 * Enumeration representing user roles for Role-Based Access Control (RBAC).
 */
public enum UserRole {
    ADMIN("Administrator"),
    FACULTY("Faculty Member"),
    STUDENT("Enrolled Student");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
