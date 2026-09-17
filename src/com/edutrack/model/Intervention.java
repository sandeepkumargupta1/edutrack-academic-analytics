package com.edutrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entity representing an active academic intervention assignment.
 */
public class Intervention implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String studentRegNumber;
    private final String studentName;
    private final String title;
    private final String strategyName;
    private InterventionStatus status;
    private String notes;
    private final String assignedMentor;
    private final LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    public Intervention(String id, String studentRegNumber, String studentName, String title,
                        String strategyName, String assignedMentor, String notes) {
        this.id = id;
        this.studentRegNumber = studentRegNumber;
        this.studentName = studentName;
        this.title = title;
        this.strategyName = strategyName;
        this.status = InterventionStatus.PENDING;
        this.assignedMentor = assignedMentor;
        this.notes = notes != null ? notes : "";
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getStudentRegNumber() {
        return studentRegNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getTitle() {
        return title;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public InterventionStatus getStatus() {
        return status;
    }

    public void setStatus(InterventionStatus status) {
        this.status = status;
        if (status == InterventionStatus.RESOLVED) {
            this.resolvedAt = LocalDateTime.now();
        }
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAssignedMentor() {
        return assignedMentor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public String getFormattedCreatedAt() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s for %s (%s) - Status: %s",
                id, title, studentName, studentRegNumber, status.getDisplayStatus());
    }
}
