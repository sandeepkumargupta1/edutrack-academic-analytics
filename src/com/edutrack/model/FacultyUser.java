package com.edutrack.model;

import java.util.Arrays;
import java.util.List;

/**
 * Concrete implementation representing Faculty members.
 * Demonstrates OOP Inheritance and Polymorphic capability definition.
 */
public class FacultyUser extends User {
    private static final long serialVersionUID = 1L;

    private String department;
    private String employeeId;

    public FacultyUser(String username, String passwordHash, String fullName, String email, String department, String employeeId) {
        super(username, passwordHash, fullName, email, UserRole.FACULTY);
        this.department = department != null ? department : "Computer Science & Engineering";
        this.employeeId = employeeId != null ? employeeId : "FAC" + username.toUpperCase();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    @Override
    public List<String> getDashboardCapabilities() {
        return Arrays.asList(
            "VIEW_STUDENTS",
            "UPDATE_MARKS",
            "RUN_ANALYTICS",
            "TRIGGER_INTERVENTIONS",
            "EXPORT_REPORTS"
        );
    }
}
