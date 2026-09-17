package com.edutrack.cli;

/**
 * Enumeration of available top-level navigation choices in the CLI.
 */
public enum MenuOption {
    VIEW_STUDENTS(1, "View Enrolled Student Directory"),
    SEARCH_STUDENT(2, "Search Student by Reg No / Name / Department"),
    VIEW_STUDENT_PROFILE(3, "Detailed Student Profile & Risk Breakdown"),
    ADD_STUDENT(4, "Register New Student Profile"),
    UPDATE_MARKS(5, "Update Continuous Assessment Marks & Attendance"),
    COHORT_ANALYTICS(6, "Cohort Analytics & ASCII Visual Charts"),
    RISK_ASSESSMENT(7, "Run Early Risk Detection & Alert Roster"),
    GENERATE_INTERVENTIONS(8, "Generate Automated Academic Interventions"),
    MANAGE_INTERVENTIONS(9, "View & Manage Intervention Action Plans"),
    EXPORT_CSV(10, "Export Performance & Risk Report to CSV"),
    LOGOUT(11, "Logout / Switch User"),
    EXIT(0, "Exit Application");

    private final int code;
    private final String label;

    MenuOption(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static MenuOption fromCode(int code) {
        for (MenuOption opt : values()) {
            if (opt.code == code) return opt;
        }
        return null;
    }
}
