package com.edutrack.cli;

import com.edutrack.exception.AuthenticationException;
import com.edutrack.exception.DuplicateRecordException;
import com.edutrack.exception.EduTrackException;
import com.edutrack.exception.StudentNotFoundException;
import com.edutrack.model.*;
import com.edutrack.service.AnalyticsEngine;
import com.edutrack.service.AuthService;
import com.edutrack.service.InterventionService;
import com.edutrack.service.StudentService;
import com.edutrack.util.AnsiColor;
import com.edutrack.util.AsciiChartRenderer;
import com.edutrack.util.TableRenderer;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Controller orchestrating terminal user interactions, rendering views,
 * and dispatching commands to underlying domain services.
 */
public class CliController {

    private final StudentService studentService;
    private final AnalyticsEngine analyticsEngine;
    private final InterventionService interventionService;
    private final AuthService authService;
    private final Scanner scanner;

    public CliController(StudentService studentService,
                         AnalyticsEngine analyticsEngine,
                         InterventionService interventionService,
                         AuthService authService) {
        this.studentService = studentService;
        this.analyticsEngine = analyticsEngine;
        this.interventionService = interventionService;
        this.authService = authService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        printBanner();
        while (true) {
            if (!authService.isAuthenticated()) {
                handleLogin();
            } else {
                handleMainMenu();
            }
        }
    }

    private void printBanner() {
        System.out.println(AnsiColor.CYAN + "================================================================================" + AnsiColor.RESET);
        System.out.println(AnsiColor.bold("   EduTrack - Academic Performance Analytics & Student Intervention Platform"));
        System.out.println(AnsiColor.DIM + "   VITyarthi Flipped Course Evaluation Edition | Pure Java CLI System" + AnsiColor.RESET);
        System.out.println(AnsiColor.CYAN + "================================================================================" + AnsiColor.RESET);
    }

    private void handleLogin() {
        System.out.println("\n" + AnsiColor.bold(">>> SYSTEM LOGIN"));
        System.out.println(AnsiColor.DIM + "Default Test Credentials: [faculty / admin123], [admin / root123], [student / student123]" + AnsiColor.RESET);

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = authService.login(username, password);
            System.out.println(AnsiColor.green("\n[SUCCESS] Welcome, " + user.getFullName() + " (" + user.getRole().getDisplayName() + ")!"));
        } catch (AuthenticationException e) {
            System.out.println(AnsiColor.red("\n[ERROR] Login failed: " + e.getMessage()));
        }
    }

    private void handleMainMenu() {
        User user = authService.getCurrentUser();
        System.out.println("\n" + AnsiColor.bold("=== MAIN MENU | Logged in as: " + user.getUsername() + " (" + user.getRole().name() + ") ==="));

        for (MenuOption opt : MenuOption.values()) {
            System.out.printf(" [%2d] %s%n", opt.getCode(), opt.getLabel());
        }
        System.out.print(AnsiColor.bold("\nEnter choice: "));

        String input = scanner.nextLine().trim();
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println(AnsiColor.red("[!] Please enter a valid numerical menu choice."));
            return;
        }

        MenuOption selected = MenuOption.fromCode(choice);
        if (selected == null) {
            System.out.println(AnsiColor.red("[!] Unknown option selected."));
            return;
        }

        switch (selected) {
            case VIEW_STUDENTS:
                viewStudentsTable(studentService.getAllStudents());
                break;
            case SEARCH_STUDENT:
                searchStudents();
                break;
            case VIEW_STUDENT_PROFILE:
                viewStudentProfile();
                break;
            case ADD_STUDENT:
                addNewStudent();
                break;
            case UPDATE_MARKS:
                updateMarks();
                break;
            case COHORT_ANALYTICS:
                showCohortAnalytics();
                break;
            case RISK_ASSESSMENT:
                showRiskAssessmentRoster();
                break;
            case GENERATE_INTERVENTIONS:
                triggerInterventions();
                break;
            case MANAGE_INTERVENTIONS:
                manageInterventions();
                break;
            case EXPORT_CSV:
                exportDataCsv();
                break;
            case LOGOUT:
                authService.logout();
                System.out.println(AnsiColor.yellow("\nLogged out successfully."));
                break;
            case EXIT:
                System.out.println(AnsiColor.cyan("\nThank you for using EduTrack. Terminating session. Goodbye!"));
                System.exit(0);
                break;
        }
    }

    private void viewStudentsTable(List<Student> students) {
        System.out.println("\n" + AnsiColor.bold("--- ENROLLED STUDENT DIRECTORY (" + students.size() + " Students) ---"));
        List<String> headers = Arrays.asList("Reg No", "Name", "Department", "Sem", "CGPA", "Attendance", "Composite/100", "Risk Level");
        List<List<String>> rows = new ArrayList<>();

        for (Student s : students) {
            AcademicRecord r = s.getAcademicRecord();
            RiskAssessment ra = s.getRiskAssessment();
            if (ra == null && r != null) {
                ra = analyticsEngine.assessStudentRisk(s);
            }

            String attStr = (r != null) ? String.format("%.1f%%", r.getAttendancePercentage()) : "N/A";
            if (r != null && r.isAttendanceDeficient()) {
                attStr = AnsiColor.red(attStr);
            }

            String compStr = (r != null) ? String.format("%.1f", r.calculateCompositeScore()) : "N/A";
            String riskStr = "Unassessed";
            if (ra != null) {
                if (ra.getRiskLevel() == RiskLevel.HIGH) riskStr = AnsiColor.red("HIGH");
                else if (ra.getRiskLevel() == RiskLevel.MODERATE) riskStr = AnsiColor.yellow("MODERATE");
                else riskStr = AnsiColor.green("LOW");
            }

            rows.add(Arrays.asList(
                    s.getRegNumber(),
                    s.getName(),
                    s.getDepartment(),
                    String.valueOf(s.getSemester()),
                    String.format("%.2f", s.getCgpa()),
                    attStr,
                    compStr,
                    riskStr
            ));
        }
        System.out.print(TableRenderer.renderTable(headers, rows));
    }

    private void searchStudents() {
        System.out.print("\nEnter search keyword (Reg No / Name / Department): ");
        String kw = scanner.nextLine().trim();
        List<Student> results = studentService.searchStudents(kw);
        System.out.println(AnsiColor.cyan("Found " + results.size() + " matching records:"));
        viewStudentsTable(results);
    }

    private void viewStudentProfile() {
        System.out.print("\nEnter Student Registration Number (e.g. 23BCE1003): ");
        String reg = scanner.nextLine().trim().toUpperCase();

        try {
            Student s = studentService.getStudent(reg);
            AcademicRecord r = s.getAcademicRecord();
            RiskAssessment ra = s.getRiskAssessment();
            if (ra == null && r != null) {
                ra = analyticsEngine.assessStudentRisk(s);
            }

            System.out.println("\n" + AnsiColor.bold("================================================================================"));
            System.out.printf("   STUDENT ACADEMIC DOSSIER: %s (%s)%n", s.getName(), s.getRegNumber());
            System.out.println("================================================================================");
            System.out.printf(" Department: %s | Semester: %d | CGPA: %.2f%n", s.getDepartment(), s.getSemester(), s.getCgpa());
            System.out.printf(" Faculty Mentor: %s | Email: %s%n", s.getMentorName(), s.getEmail());

            if (r != null) {
                System.out.println("\n" + AnsiColor.bold("--- Continuous Assessment Record (Course: " + r.getCourseCode() + ") ---"));
                System.out.printf(" Internal Test 1: %.1f / 50.0   | Internal Test 2: %.1f / 50.0%n", r.getInternalTest1(), r.getInternalTest2());
                System.out.printf(" Assignment:      %.1f / 20.0   | Lab Practical:   %.1f / 30.0%n", r.getAssignmentScore(), r.getLabScore());
                System.out.printf(" Quiz Score:      %.1f / 20.0   | Study Hours/Wk:  %.1f hrs%n", r.getQuizScore(), r.getStudyHoursPerWeek());
                System.out.printf(" Attendance:      %.1f%%         | Active Backlogs: %d%n", r.getAttendancePercentage(), r.getBacklogsCount());
                System.out.printf(" Normalized Continuous Score: %.1f / 100.0%n", r.calculateCompositeScore());
            }

            if (ra != null) {
                System.out.println("\n" + AnsiColor.bold("--- Predictive Risk Analytics & Forecasting ---"));
                System.out.printf(" Risk Classification: %s%n", ra.getRiskLevel() == RiskLevel.HIGH ? AnsiColor.red("HIGH RISK") :
                        (ra.getRiskLevel() == RiskLevel.MODERATE ? AnsiColor.yellow("MODERATE RISK") : AnsiColor.green("LOW RISK")));
                System.out.printf(" Failure Probability: %s%n", AsciiChartRenderer.renderRiskGauge(ra.getFailureProbability()));
                System.out.printf(" Projected Final Score: %.1f / 100.0%n", ra.getProjectedFinalScore());
                System.out.println(" Key Risk Drivers:");
                for (String factor : ra.getKeyRiskFactors()) {
                    System.out.println("   • " + factor);
                }
                System.out.println(" Recommended Action: " + AnsiColor.bold(ra.getRecommendation()));
            }

            List<Intervention> interventions = s.getInterventions();
            if (!interventions.isEmpty()) {
                System.out.println("\n" + AnsiColor.bold("--- Active & Assigned Interventions ---"));
                for (Intervention i : interventions) {
                    System.out.printf(" • [%s] %s (Status: %s) - %s%n", i.getId(), i.getTitle(), i.getStatus().getDisplayStatus(), i.getNotes());
                }
            }
            System.out.println("================================================================================");

        } catch (StudentNotFoundException e) {
            System.out.println(AnsiColor.red("[ERROR] " + e.getMessage()));
        }
    }

    private void addNewStudent() {
        System.out.println("\n" + AnsiColor.bold(">>> REGISTER NEW STUDENT"));
        try {
            System.out.print("Registration Number (e.g. 23BCE1021): ");
            String reg = scanner.nextLine().trim().toUpperCase();
            if (!InputValidator.isValidRegNumber(reg)) {
                System.out.println(AnsiColor.red("[!] Invalid registration format. Must be like 23BCE1021."));
                return;
            }

            System.out.print("Full Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Email Address: ");
            String email = scanner.nextLine().trim();
            if (!InputValidator.isValidEmail(email)) {
                System.out.println(AnsiColor.red("[!] Invalid email format."));
                return;
            }

            System.out.print("Department [Computer Science & Engineering]: ");
            String dept = scanner.nextLine().trim();
            if (dept.isEmpty()) dept = "Computer Science & Engineering";

            System.out.print("Semester (1-10) [5]: ");
            String semInput = scanner.nextLine().trim();
            int sem = semInput.isEmpty() ? 5 : Integer.parseInt(semInput);

            System.out.print("CGPA (0.0 - 10.0) [8.0]: ");
            String cgpaInput = scanner.nextLine().trim();
            double cgpa = cgpaInput.isEmpty() ? 8.0 : Double.parseDouble(cgpaInput);

            System.out.print("Faculty Mentor [Dr. K. Raman]: ");
            String mentor = scanner.nextLine().trim();
            if (mentor.isEmpty()) mentor = "Dr. K. Raman";

            Student created = studentService.createStudent(reg, name, email, dept, sem, cgpa, mentor);
            System.out.println(AnsiColor.green("\n[SUCCESS] Registered new student: " + created));

        } catch (DuplicateRecordException e) {
            System.out.println(AnsiColor.red("[ERROR] " + e.getMessage()));
        } catch (Exception e) {
            System.out.println(AnsiColor.red("[ERROR] Failed to create student: " + e.getMessage()));
        }
    }

    private void updateMarks() {
        System.out.print("\nEnter Student Registration Number to update: ");
        String reg = scanner.nextLine().trim().toUpperCase();

        try {
            Student s = studentService.getStudent(reg);
            AcademicRecord existing = s.getAcademicRecord();

            System.out.printf("Updating assessment marks for %s (%s):%n", s.getName(), s.getRegNumber());

            double it1 = promptDouble("Internal Test 1 (0-50)", existing != null ? existing.getInternalTest1() : 25.0, 0.0, 50.0);
            double it2 = promptDouble("Internal Test 2 (0-50)", existing != null ? existing.getInternalTest2() : 25.0, 0.0, 50.0);
            double asgn = promptDouble("Assignment Score (0-20)", existing != null ? existing.getAssignmentScore() : 15.0, 0.0, 20.0);
            double lab = promptDouble("Lab Practical Score (0-30)", existing != null ? existing.getLabScore() : 22.0, 0.0, 30.0);
            double quiz = promptDouble("Quiz Score (0-20)", existing != null ? existing.getQuizScore() : 14.0, 0.0, 20.0);
            double att = promptDouble("Attendance % (0-100)", existing != null ? existing.getAttendancePercentage() : 85.0, 0.0, 100.0);
            double hours = promptDouble("Study Hours/Week", existing != null ? existing.getStudyHoursPerWeek() : 10.0, 0.0, 80.0);
            int backlogs = promptInt("Active Backlogs Count", existing != null ? existing.getBacklogsCount() : 0, 0, 15);

            String course = existing != null ? existing.getCourseCode() : "CSE2001";
            AcademicRecord newRec = new AcademicRecord(course, it1, it2, asgn, lab, quiz, att, hours, backlogs);
            studentService.updateAcademicRecord(reg, newRec);

            // Re-run analytics
            RiskAssessment ra = analyticsEngine.assessStudentRisk(s);

            System.out.println(AnsiColor.green("\n[SUCCESS] Marks updated successfully."));
            System.out.printf("Composite Score: %.1f/100 | Risk Level: %s | Failure Prob: %.1f%%%n",
                    newRec.calculateCompositeScore(), ra.getRiskLevel().name(), ra.getFailureProbability() * 100.0);

        } catch (EduTrackException e) {
            System.out.println(AnsiColor.red("[ERROR] " + e.getMessage()));
        }
    }

    private double promptDouble(String label, double defaultVal, double min, double max) {
        System.out.printf("%s [%.1f]: ", label, defaultVal);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultVal;
        try {
            double val = Double.parseDouble(input);
            if (val < min || val > max) {
                System.out.printf("[!] Out of range [%.1f - %.1f]. Using default %.1f.%n", min, max, defaultVal);
                return defaultVal;
            }
            return val;
        } catch (NumberFormatException e) {
            System.out.printf("[!] Invalid numeric format. Using default %.1f.%n", defaultVal);
            return defaultVal;
        }
    }

    private int promptInt(String label, int defaultVal, int min, int max) {
        System.out.printf("%s [%d]: ", label, defaultVal);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultVal;
        try {
            int val = Integer.parseInt(input);
            if (val < min || val > max) {
                System.out.printf("[!] Out of range [%d - %d]. Using default %d.%n", min, max, defaultVal);
                return defaultVal;
            }
            return val;
        } catch (NumberFormatException e) {
            System.out.printf("[!] Invalid integer format. Using default %d.%n", defaultVal);
            return defaultVal;
        }
    }

    private void showCohortAnalytics() {
        List<Student> students = studentService.getAllStudents();
        analyticsEngine.runCohortRiskAssessment(students);

        System.out.println("\n" + AnsiColor.bold("================================================================================"));
        System.out.println(AnsiColor.bold("                   COHORT ANALYTICS & STATISTICAL INDICATORS"));
        System.out.println("================================================================================");

        double avgCgpa = analyticsEngine.getAverageCgpa(students);
        double avgAtt = analyticsEngine.getAverageAttendance(students);
        double avgComp = analyticsEngine.getAverageCompositeScore(students);

        System.out.printf(" Total Enrolled Students: %d%n", students.size());
        System.out.printf(" Cohort Average CGPA:     %.2f / 10.0%n", avgCgpa);
        System.out.printf(" Cohort Average Att.:     %.1f%%%n", avgAtt);
        System.out.printf(" Cohort Composite Score:  %.1f / 100.0%n%n", avgComp);

        // Render Risk Distribution Chart
        Map<RiskLevel, Integer> riskMap = analyticsEngine.getRiskDistribution(students);
        Map<String, Integer> chartRisk = new LinkedHashMap<>();
        chartRisk.put("High Risk (Urgent)", riskMap.get(RiskLevel.HIGH));
        chartRisk.put("Moderate Risk", riskMap.get(RiskLevel.MODERATE));
        chartRisk.put("Low Risk (Stable)", riskMap.get(RiskLevel.LOW));
        System.out.print(AsciiChartRenderer.renderBarChart("COHORT ACADEMIC RISK BREAKDOWN", chartRisk, 30));

        // Render Grade Distribution Chart
        Map<String, Integer> grades = analyticsEngine.getGradeDistribution(students);
        System.out.print(AsciiChartRenderer.renderBarChart("CONTINUOUS ASSESSMENT GRADE HISTOGRAM", grades, 30));
        System.out.println("================================================================================");
    }

    private void showRiskAssessmentRoster() {
        List<Student> students = studentService.getAllStudents();
        analyticsEngine.runCohortRiskAssessment(students);
        List<Student> atRisk = analyticsEngine.getAtRiskStudents(students);

        System.out.println("\n" + AnsiColor.bold("--- EARLY RISK DETECTION & EARLY-WARNING ALERT ROSTER (" + atRisk.size() + " Flagged) ---"));

        List<String> headers = Arrays.asList("Reg No", "Student Name", "Att.", "Comp/100", "Risk Level", "Fail Prob.", "Primary Risk Drivers");
        List<List<String>> rows = new ArrayList<>();

        for (Student s : atRisk) {
            RiskAssessment ra = s.getRiskAssessment();
            AcademicRecord ar = s.getAcademicRecord();

            String riskBadge = ra.getRiskLevel() == RiskLevel.HIGH ? AnsiColor.red("HIGH") : AnsiColor.yellow("MODERATE");
            String att = (ar != null) ? String.format("%.1f%%", ar.getAttendancePercentage()) : "-";
            String comp = (ar != null) ? String.format("%.1f", ar.calculateCompositeScore()) : "-";
            String drivers = !ra.getKeyRiskFactors().isEmpty() ? ra.getKeyRiskFactors().get(0) : "Multiple factors";

            rows.add(Arrays.asList(
                    s.getRegNumber(),
                    s.getName(),
                    att,
                    comp,
                    riskBadge,
                    String.format("%.1f%%", ra.getFailureProbability() * 100.0),
                    drivers
            ));
        }

        System.out.print(TableRenderer.renderTable(headers, rows));
    }

    private void triggerInterventions() {
        List<Student> students = studentService.getAllStudents();
        analyticsEngine.runCohortRiskAssessment(students);

        System.out.println("\n" + AnsiColor.bold(">>> EXECUTING AUTOMATED INTERVENTION STRATEGIES (GoF Strategy Pattern)"));
        int generated = interventionService.generateBatchInterventions(students);

        System.out.printf(AnsiColor.green("[SUCCESS] Generated %d tailored academic intervention plans for at-risk students.%n"), generated);
        manageInterventions();
    }

    private void manageInterventions() {
        List<Intervention> list = interventionService.getAllInterventions();
        System.out.println("\n" + AnsiColor.bold("--- ACTIVE & ASSIGNED INTERVENTION PLANS (" + list.size() + " Total) ---"));

        List<String> headers = Arrays.asList("ID", "Reg No", "Student Name", "Intervention Title", "Strategy", "Status", "Assigned Mentor");
        List<List<String>> rows = new ArrayList<>();

        for (Intervention i : list) {
            String statusCol;
            if (i.getStatus() == InterventionStatus.RESOLVED) statusCol = AnsiColor.green("RESOLVED");
            else if (i.getStatus() == InterventionStatus.IN_PROGRESS) statusCol = AnsiColor.yellow("IN PROGRESS");
            else statusCol = AnsiColor.red("PENDING");

            rows.add(Arrays.asList(
                    i.getId(),
                    i.getStudentRegNumber(),
                    i.getStudentName(),
                    i.getTitle(),
                    i.getStrategyName(),
                    statusCol,
                    i.getAssignedMentor()
            ));
        }

        System.out.print(TableRenderer.renderTable(headers, rows));

        System.out.print("Update intervention status? Enter Intervention ID (or press Enter to return): ");
        String id = scanner.nextLine().trim().toUpperCase();
        if (id.isEmpty()) return;

        Optional<Intervention> target = interventionService.getInterventionById(id);
        if (target.isEmpty()) {
            System.out.println(AnsiColor.red("[!] Intervention ID not found."));
            return;
        }

        System.out.println("Select New Status:");
        System.out.println(" [1] IN PROGRESS");
        System.out.println(" [2] RESOLVED");
        System.out.println(" [3] ESCALATED");
        System.out.print("Choice: ");
        String ch = scanner.nextLine().trim();
        InterventionStatus newStatus = InterventionStatus.IN_PROGRESS;
        if ("2".equals(ch)) newStatus = InterventionStatus.RESOLVED;
        else if ("3".equals(ch)) newStatus = InterventionStatus.ESCALATED;

        System.out.print("Action / Meeting Notes: ");
        String notes = scanner.nextLine().trim();

        interventionService.updateStatus(id, newStatus, notes);
        System.out.println(AnsiColor.green("[SUCCESS] Updated intervention " + id + " to " + newStatus.getDisplayStatus()));
    }

    private void exportDataCsv() {
        File exportDir = new File("data/export");
        if (!exportDir.exists()) exportDir.mkdirs();

        File exportFile = new File(exportDir, "edutrack_export_" + System.currentTimeMillis() + ".csv");
        try {
            studentService.exportCsv(exportFile);
            System.out.println(AnsiColor.green("\n[SUCCESS] Successfully exported student records to: " + exportFile.getAbsolutePath()));
        } catch (IOException e) {
            System.out.println(AnsiColor.red("[ERROR] Export failed: " + e.getMessage()));
        }
    }

    /**
     * Non-interactive headless execution mode.
     * Evaluates all data and prints complete summary to stdout without waiting for keyboard input.
     */
    public void executeHeadlessReport() {
        printBanner();
        System.out.println(AnsiColor.bold("\n>>> EXECUTING HEADLESS AUTOMATED COHORT EVALUATION REPORT <<<\n"));

        List<Student> students = studentService.getAllStudents();
        analyticsEngine.runCohortRiskAssessment(students);
        int generatedInterventions = interventionService.generateBatchInterventions(students);

        viewStudentsTable(students);
        showCohortAnalytics();
        showRiskAssessmentRoster();

        System.out.println("\n" + AnsiColor.bold("--- INTERVENTION AUDIT SUMMARY ---"));
        System.out.printf("Total Actionable Interventions Generated: %d%n", generatedInterventions);
        List<Intervention> all = interventionService.getAllInterventions();
        for (int i = 0; i < Math.min(5, all.size()); i++) {
            System.out.println(" • " + all.get(i));
        }

        System.out.println(AnsiColor.green("\n[PASS] Headless Evaluation Completed Successfully (Exit code 0)."));
    }
}
