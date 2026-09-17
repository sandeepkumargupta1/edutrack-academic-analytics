package com.edutrack.model;

/**
 * Lifecycle states of academic intervention assignments.
 */
public enum InterventionStatus {
    PENDING("Action Pending"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Successfully Resolved"),
    ESCALATED("Escalated to HoD / Dean");

    private final String displayStatus;

    InterventionStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }
}
