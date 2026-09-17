package com.edutrack.model;

/**
 * Risk classification levels for student academic performance evaluation.
 */
public enum RiskLevel {
    LOW("Low Risk", "Stable academic performance; regular progress expected"),
    MODERATE("Moderate Risk", "Performance dipping in certain assessments; advisory support needed"),
    HIGH("High Risk", "Severe failure/detention risk; mandatory early intervention required");

    private final String label;
    private final String description;

    RiskLevel(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
