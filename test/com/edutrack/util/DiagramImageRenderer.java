package com.edutrack.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Headless Java AWT/Graphics2D utility to generate crisp, high-resolution PNG diagrams
 * directly into the docs/ directory without requiring third-party tools or Python.
 */
public class DiagramImageRenderer {

    public static void main(String[] args) {
        File docsDir = new File("docs");
        if (!docsDir.exists()) docsDir.mkdirs();

        try {
            renderArchitectureDiagram(new File(docsDir, "architecture-diagram.png"));
            renderWorkflowDiagram(new File(docsDir, "workflow-diagram.png"));
            renderUseCaseDiagram(new File(docsDir, "use-case-diagram.png"));
            renderClassDiagram(new File(docsDir, "class-diagram.png"));
            renderSequenceDiagram(new File(docsDir, "sequence-diagram.png"));
            renderErDiagram(new File(docsDir, "er-diagram.png"));
            System.out.println("[SUCCESS] Generated all 6 diagram PNG images in docs/");
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to generate diagram PNGs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Graphics2D createGraphics(BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return g;
    }

    private static void drawBanner(Graphics2D g, String title, String subtitle, int width) {
        g.setColor(new Color(30, 58, 138));
        g.fillRect(0, 0, width, 70);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString(title, 25, 38);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.setColor(new Color(191, 219, 254));
        g.drawString(subtitle, 25, 58);
    }

    private static void renderArchitectureDiagram(File target) throws IOException {
        int w = 1100, h = 750;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = createGraphics(img);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        drawBanner(g, "EduTrack: System Architecture Diagram", "Layered Architecture with Strategy Pattern & File-Backed In-Memory Repository", w);

        int[][] layersY = {{90, 100}, {210, 100}, {330, 105}, {455, 110}, {585, 110}};
        String[] layerNames = {
            "Presentation Layer (Terminal CLI)",
            "Service & Business Logic Layer",
            "Behavioral Strategy Layer (GoF Pattern)",
            "Domain Model & Entities Layer",
            "Persistence & Data Storage Layer"
        };
        Color[] bgColors = {
            new Color(224, 242, 254),
            new Color(220, 252, 231),
            new Color(254, 243, 199),
            new Color(243, 232, 255),
            new Color(254, 226, 226)
        };
        Color[] borderColors = {
            new Color(2, 132, 199),
            new Color(22, 163, 74),
            new Color(217, 119, 6),
            new Color(147, 51, 234),
            new Color(220, 38, 38)
        };

        String[][] layerBoxes = {
            {"CliController (Menu Loop)", "TableRenderer (ASCII Tables)", "AsciiChartRenderer (Histograms)", "InputValidator (Regex/Bounds)", "MenuOption (Nav Enums)"},
            {"AuthService (SHA-256 RBAC)", "StudentService (CRUD & Queries)", "AnalyticsEngine (Risk Scoring)", "InterventionService (Task Orchestration)"},
            {"InterventionStrategy (Interface)", "RemedialClassStrategy (Score < 50)", "AttendanceCounselingStrategy (Att < 75%)", "PeerTutoringStrategy (Backlogs)"},
            {"User / Faculty / Admin / Student", "Student Aggregate Entity", "AcademicRecord (Continuous)", "RiskAssessment (Sigmoid Prob)", "Intervention (Lifecycle)"},
            {"StudentRepository (Interface)", "InMemoryStudentRepository (O(1) Map)", "FileStudentRepository (Auto-Flush)", "CsvHandler (Parser/Writer)", "students_db.csv & seed.csv"}
        };

        for (int i = 0; i < 5; i++) {
            int y = layersY[i][0];
            int lh = layersY[i][1];

            g.setColor(new Color(248, 250, 252));
            g.fillRoundRect(20, y, w - 40, lh, 12, 12);
            g.setColor(new Color(203, 213, 225));
            g.drawRoundRect(20, y, w - 40, lh, 12, 12);

            g.setColor(borderColors[i]);
            g.setFont(new Font("SansSerif", Font.BOLD, 13));
            g.drawString(layerNames[i], 35, y + 22);

            String[] boxes = layerBoxes[i];
            int boxCount = boxes.length;
            int totalW = w - 80;
            int bw = (totalW - (boxCount - 1) * 12) / boxCount;

            for (int b = 0; b < boxCount; b++) {
                int bx = 40 + b * (bw + 12);
                int by = y + 32;
                int bh = lh - 42;

                g.setColor(bgColors[i]);
                g.fillRoundRect(bx, by, bw, bh, 8, 8);
                g.setColor(borderColors[i]);
                g.setStroke(new BasicStroke(1.5f));
                g.drawRoundRect(bx, by, bw, bh, 8, 8);

                g.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g.setColor(Color.DARK_GRAY);
                g.drawString(boxes[b], bx + 8, by + bh / 2 + 4);
            }
        }

        g.dispose();
        ImageIO.write(img, "png", target);
    }

    private static void renderWorkflowDiagram(File target) throws IOException {
        int w = 1000, h = 700;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = createGraphics(img);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        drawBanner(g, "EduTrack: End-to-End Operational Workflow", "Interactive & Headless CLI Lifecycle from Login to CSV Durability", w);

        String[] steps = {
            "1. System Startup (Check --report flag)",
            "2. User Authentication (SHA-256 Hash Verification)",
            "3. Role Authorization (Faculty / Admin / Student)",
            "4. Main Menu Operation Dispatch",
            "5. Input Data Validation (Regex & Numeric Bounds)",
            "6. Academic Record Processing (IT1, IT2, Lab, Att.)",
            "7. AnalyticsEngine: Multi-Factor Risk Calculation",
            "8. Sigmoid Failure Probability Mapping",
            "9. InterventionService: Strategy Evaluation & Generation",
            "10. Terminal Output (ANSI Tables, ASCII Histograms, Alerts)",
            "11. Persistence: Auto-Flush to students_db.csv"
        };

        Color[] stepColors = {
            new Color(224, 242, 254), new Color(254, 243, 199), new Color(220, 252, 231),
            new Color(243, 232, 255), new Color(254, 226, 226), new Color(224, 242, 254),
            new Color(254, 243, 199), new Color(254, 226, 226), new Color(220, 252, 231),
            new Color(243, 232, 255), new Color(220, 252, 231)
        };

        int y = 90;
        for (int i = 0; i < steps.length; i++) {
            g.setColor(stepColors[i]);
            g.fillRoundRect(150, y, 700, 42, 8, 8);
            g.setColor(new Color(71, 85, 105));
            g.setStroke(new BasicStroke(1.2f));
            g.drawRoundRect(150, y, 700, 42, 8, 8);

            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.setColor(new Color(15, 23, 42));
            g.drawString(steps[i], 180, y + 26);

            if (i < steps.length - 1) {
                g.setColor(new Color(100, 116, 139));
                g.drawLine(500, y + 42, 500, y + 54);
                int[] px = {496, 504, 500};
                int[] py = {54, 54, 58};
                g.fillPolygon(px, py, 3);
            }
            y += 54;
        }

        g.dispose();
        ImageIO.write(img, "png", target);
    }

    private static void renderUseCaseDiagram(File target) throws IOException {
        int w = 950, h = 650;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = createGraphics(img);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        drawBanner(g, "EduTrack: UML Use Case Diagram", "Core Actors and Authorized Capabilities in Academic Management", w);

        // Draw Boundary Box
        g.setColor(new Color(248, 250, 252));
        g.fillRoundRect(220, 90, 680, 530, 12, 12);
        g.setColor(new Color(148, 163, 184));
        g.drawRoundRect(220, 90, 680, 530, 12, 12);
        g.setFont(new Font("SansSerif", Font.BOLD, 13));
        g.setColor(new Color(30, 58, 138));
        g.drawString("EduTrack CLI Application Boundary", 240, 115);

        // Actors on left
        String[] actors = {"Faculty Member", "Administrator", "Enrolled Student"};
        Color[] actorColors = {new Color(37, 99, 235), new Color(220, 38, 38), new Color(22, 163, 74)};
        int[] actorY = {180, 360, 520};

        for (int i = 0; i < 3; i++) {
            g.setColor(actorColors[i]);
            g.fillOval(70, actorY[i] - 30, 40, 40);
            g.drawLine(90, actorY[i] + 10, 90, actorY[i] + 40);
            g.drawLine(70, actorY[i] + 20, 110, actorY[i] + 20);
            g.drawLine(90, actorY[i] + 40, 70, actorY[i] + 70);
            g.drawLine(90, actorY[i] + 40, 110, actorY[i] + 70);

            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString(actors[i], 50, actorY[i] + 90);
        }

        // Use Cases
        String[] useCases = {
            "UC-1: Authenticate via Credentials",
            "UC-2: View Enrolled Student Directory",
            "UC-3: Search Students by Keyword",
            "UC-4: Inspect Comprehensive Student Dossier",
            "UC-5: Record Assessment Marks & Attendance",
            "UC-6: Review Cohort Analytics & ASCII Histograms",
            "UC-7: Run Early Risk Detection & Alert Roster",
            "UC-8: Trigger Automated Interventions (Strategy)",
            "UC-9: Manage Intervention Lifecycle & Notes",
            "UC-10: Register New Student Profiles",
            "UC-11: Export Cohort Data to CSV",
            "UC-12: View Self Academic Performance & Attendance"
        };

        int uY = 135;
        for (int i = 0; i < useCases.length; i++) {
            g.setColor(new Color(254, 249, 195));
            g.fillRoundRect(280, uY, 560, 32, 16, 16);
            g.setColor(new Color(202, 138, 4));
            g.drawRoundRect(280, uY, 560, 32, 16, 16);

            g.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g.setColor(Color.BLACK);
            g.drawString(useCases[i], 310, uY + 20);

            // Connect lines
            g.setColor(new Color(148, 163, 184));
            if (i <= 8 || i == 10) {
                g.drawLine(120, actorY[0] + 10, 280, uY + 16);
            }
            if (i == 0 || i == 1 || i == 2 || i == 9 || i == 10) {
                g.drawLine(120, actorY[1] + 10, 280, uY + 16);
            }
            if (i == 0 || i == 11) {
                g.drawLine(120, actorY[2] + 10, 280, uY + 16);
            }

            uY += 40;
        }

        g.dispose();
        ImageIO.write(img, "png", target);
    }

    private static void renderClassDiagram(File target) throws IOException {
        int w = 1150, h = 800;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = createGraphics(img);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        drawBanner(g, "EduTrack: Comprehensive UML Class Diagram", "Real Java Classes, Entities, Services, Strategy Pattern & Repository Architecture", w);

        // Draw Class Boxes
        drawClassBox(g, 30, 90, 220, 160, "User (Abstract)",
                new String[]{"- username: String", "- passwordHash: String", "- fullName: String", "- role: UserRole"},
                new String[]{"+ getUsername(): String", "+ getRole(): UserRole", "+ getDashboardCapabilities()*"},
                new Color(243, 232, 255), new Color(147, 51, 234));

        drawClassBox(g, 280, 90, 270, 210, "Student (Aggregate Entity)",
                new String[]{"- regNumber: String", "- name: String", "- email: String", "- cgpa: double", "- academicRecord: AcademicRecord", "- riskAssessment: RiskAssessment"},
                new String[]{"+ getRegNumber(): String", "+ getCgpa(): double", "+ setAcademicRecord(rec): void", "+ addIntervention(i): void"},
                new Color(224, 242, 254), new Color(2, 132, 199));

        drawClassBox(g, 580, 90, 260, 210, "AcademicRecord",
                new String[]{"- courseCode: String", "- internalTest1: double", "- internalTest2: double", "- labScore: double", "- attendancePercentage: double", "- backlogsCount: int"},
                new String[]{"+ calculateCompositeScore(): double", "+ isAttendanceDeficient(): boolean", "+ isFailingComposite(): boolean"},
                new Color(224, 242, 254), new Color(2, 132, 199));

        drawClassBox(g, 870, 90, 250, 170, "RiskAssessment",
                new String[]{"- riskLevel: RiskLevel", "- failureProbability: double", "- projectedFinalScore: double", "- keyRiskFactors: List<String>"},
                new String[]{"+ getRiskLevel(): RiskLevel", "+ getFailureProbability(): double", "+ getKeyRiskFactors(): List"},
                new Color(254, 243, 199), new Color(217, 119, 6));

        drawClassBox(g, 30, 320, 240, 180, "StudentService",
                new String[]{"- repository: StudentRepository"},
                new String[]{"+ getAllStudents(): List", "+ getStudent(reg): Student", "+ createStudent(...): Student", "+ updateAcademicRecord(...) void", "+ searchStudents(kw): List"},
                new Color(220, 252, 231), new Color(22, 163, 74));

        drawClassBox(g, 300, 320, 260, 180, "AnalyticsEngine",
                new String[]{"- HIGH_RISK_THRESHOLD: double"},
                new String[]{"+ assessStudentRisk(s): RiskAssessment", "+ runCohortRiskAssessment(list) void", "+ getAverageCgpa(list): double", "+ getRiskDistribution(list): Map"},
                new Color(220, 252, 231), new Color(22, 163, 74));

        drawClassBox(g, 590, 320, 250, 180, "InterventionService",
                new String[]{"- strategies: List<Strategy>", "- registry: Map<String, Interv>"},
                new String[]{"+ generateInterventions(s): List", "+ generateBatchInterventions(list) int", "+ updateStatus(id, st, notes): bool"},
                new Color(220, 252, 231), new Color(22, 163, 74));

        drawClassBox(g, 870, 320, 250, 180, "«interface» InterventionStrategy",
                new String[]{},
                new String[]{"+ getStrategyName(): String", "+ isApplicable(s): boolean", "+ createIntervention(s, id): Interv."},
                new Color(254, 243, 199), new Color(217, 119, 6));

        drawClassBox(g, 30, 540, 260, 210, "«interface» StudentRepository",
                new String[]{},
                new String[]{"+ save(s): Student", "+ findByRegNumber(reg): Optional", "+ findAll(): List<Student>", "+ deleteByRegNumber(reg): bool", "+ count(): int", "+ flush(): void"},
                new Color(254, 226, 226), new Color(220, 38, 38));

        drawClassBox(g, 320, 540, 250, 210, "FileStudentRepository",
                new String[]{"- storageFile: File", "- seedFile: File", "- studentStore: ConcurrentHashMap"},
                new String[]{"+ save(s): Student", "+ deleteByRegNumber(reg): bool", "+ flush(): void", "+ loadInitialData(): void"},
                new Color(254, 226, 226), new Color(220, 38, 38));

        drawClassBox(g, 600, 540, 520, 210, "Strategy Pattern Implementations",
                new String[]{
                    "• RemedialClassStrategy: Triggers if IT1 < 22, IT2 < 22, or Composite < 50",
                    "• AttendanceCounselingStrategy: Triggers if Attendance < 75.0% threshold",
                    "• PeerTutoringStrategy: Triggers if Moderate Risk or Backlogs > 0"
                },
                new String[]{
                    "+ All implement InterventionStrategy polymorphic contract",
                    "+ Concrete action plans with assigned mentors & follow-up tracking"
                },
                new Color(254, 243, 199), new Color(217, 119, 6));

        g.dispose();
        ImageIO.write(img, "png", target);
    }

    private static void drawClassBox(Graphics2D g, int x, int y, int w, int h, String title,
                                     String[] fields, String[] methods, Color bg, Color border) {
        g.setColor(bg);
        g.fillRoundRect(x, y, w, h, 8, 8);
        g.setColor(border);
        g.setStroke(new BasicStroke(1.2f));
        g.drawRoundRect(x, y, w, h, 8, 8);

        // Header
        g.setColor(border);
        g.fillRect(x, y, w, 26);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 11));
        g.drawString(title, x + 8, y + 17);

        // Content
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(Color.BLACK);
        int curY = y + 42;
        for (String f : fields) {
            g.drawString(f, x + 8, curY);
            curY += 16;
        }

        if (fields.length > 0 && methods.length > 0) {
            g.setColor(new Color(203, 213, 225));
            g.drawLine(x, curY - 6, x + w, curY - 6);
            curY += 6;
        }

        g.setColor(Color.BLACK);
        for (String m : methods) {
            g.drawString(m, x + 8, curY);
            curY += 16;
        }
    }

    private static void renderSequenceDiagram(File target) throws IOException {
        int w = 1050, h = 680;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = createGraphics(img);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        drawBanner(g, "EduTrack: UML Sequence Diagram", "End-to-End Execution: Mark Entry, Risk Assessment & Strategy Intervention", w);

        String[] participants = {"Faculty", "CliController", "StudentService", "FileStudentRepo", "AnalyticsEngine", "InterventionService", "Strategy"};
        int[] posX = {80, 220, 380, 530, 680, 830, 960};

        // Draw Lifelines
        for (int i = 0; i < participants.length; i++) {
            g.setColor(new Color(30, 58, 138));
            g.fillRoundRect(posX[i] - 55, 85, 110, 30, 6, 6);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 10));
            g.drawString(participants[i], posX[i] - 45, 104);

            g.setColor(new Color(203, 213, 225));
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{4.0f}, 0.0f));
            g.drawLine(posX[i], 115, posX[i], 640);
        }

        g.setStroke(new BasicStroke(1.2f));

        String[][] messages = {
            {"1. updateMarks(regNo, it1, it2, lab, att)", "0", "1"},
            {"2. updateAcademicRecord(regNo, record)", "1", "2"},
            {"3. findByRegNumber(regNo)", "2", "3"},
            {"4. return Student entity", "3", "2"},
            {"5. save(student) & flush() to students_db.csv", "2", "3"},
            {"6. assessStudentRisk(student)", "1", "4"},
            {"7. calculateCompositeScore() & eval attendance", "4", "4"},
            {"8. compute failureProbability via Sigmoid (83.9%)", "4", "4"},
            {"9. return RiskAssessment (RiskLevel.HIGH)", "4", "1"},
            {"10. generateInterventionsForStudent(student)", "1", "5"},
            {"11. isApplicable(student) -> checks IT/Att criteria", "5", "6"},
            {"12. return true & createIntervention()", "6", "5"},
            {"13. return List<Intervention> (Remedial & Attendance)", "5", "1"},
            {"14. TableRenderer & AsciiChartRenderer output", "1", "0"}
        };

        int mY = 140;
        for (int i = 0; i < messages.length; i++) {
            int fromIdx = Integer.parseInt(messages[i][1]);
            int toIdx = Integer.parseInt(messages[i][2]);
            int x1 = posX[fromIdx];
            int x2 = posX[toIdx];

            if (fromIdx == toIdx) {
                g.setColor(new Color(217, 119, 6));
                g.drawArc(x1, mY - 10, 40, 20, 90, 270);
                g.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g.drawString(messages[i][0], x1 + 45, mY + 4);
            } else {
                g.setColor(new Color(30, 58, 138));
                g.drawLine(x1, mY, x2, mY);
                int dir = x2 > x1 ? 1 : -1;
                int[] px = {x2, x2 - dir * 8, x2 - dir * 8};
                int[] py = {mY, mY - 4, mY + 4};
                g.fillPolygon(px, py, 3);

                g.setFont(new Font("SansSerif", Font.PLAIN, 10));
                int labelX = Math.min(x1, x2) + 10;
                g.drawString(messages[i][0], labelX, mY - 4);
            }
            mY += 36;
        }

        g.dispose();
        ImageIO.write(img, "png", target);
    }

    private static void renderErDiagram(File target) throws IOException {
        int w = 1000, h = 650;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = createGraphics(img);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        drawBanner(g, "EduTrack: CSV Storage & In-Memory Entity Schema", "True Data Storage Layout (File-Backed CSV & Domain Value Objects)", w);

        drawClassBox(g, 40, 100, 360, 480, "Student CSV Record (students_db.csv)",
                new String[]{
                    "reg_number : VARCHAR(20) [PRIMARY KEY]",
                    "name : VARCHAR(100)",
                    "email : VARCHAR(100) [UNIQUE]",
                    "department : VARCHAR(50)",
                    "semester : INTEGER (1-10)",
                    "cgpa : DOUBLE (0.0-10.0)",
                    "mentor_name : VARCHAR(100)",
                    "course_code : VARCHAR(20)",
                    "internal_test_1 : DOUBLE (0.0-50.0)",
                    "internal_test_2 : DOUBLE (0.0-50.0)",
                    "assignment_score : DOUBLE (0.0-20.0)",
                    "lab_score : DOUBLE (0.0-30.0)",
                    "quiz_score : DOUBLE (0.0-20.0)",
                    "attendance_percentage : DOUBLE (0-100%)",
                    "study_hours_per_week : DOUBLE (>=0)",
                    "backlogs_count : INTEGER (>=0)"
                },
                new String[]{
                    "• Comma-separated UTF-8 flat file",
                    "• Auto-synced on all mutations",
                    "• ConcurrentHashMap cached in memory"
                },
                new Color(224, 242, 254), new Color(2, 132, 199));

        drawClassBox(g, 480, 100, 480, 190, "In-Memory RiskAssessment (Computed Value Object)",
                new String[]{
                    "student_reg_number : VARCHAR(20) [FOREIGN KEY]",
                    "risk_level : RiskLevel (LOW / MODERATE / HIGH)",
                    "risk_score : DOUBLE (0.0 - 100.0 continuous)",
                    "failure_probability : DOUBLE (0.0 - 1.0 via Sigmoid)",
                    "projected_final_score : DOUBLE (0.0 - 100.0)",
                    "key_risk_factors : List<String> (Causal drivers)",
                    "assessed_at : LocalDateTime"
                },
                new String[]{"1:1 Relationship with Student record"},
                new Color(254, 243, 199), new Color(217, 119, 6));

        drawClassBox(g, 480, 320, 480, 180, "In-Memory Intervention (Remedial Action Entity)",
                new String[]{
                    "id : VARCHAR(20) [PRIMARY KEY, e.g. INT-1001]",
                    "student_reg_number : VARCHAR(20) [FOREIGN KEY]",
                    "title : VARCHAR(150)",
                    "strategy_name : VARCHAR(60)",
                    "status : InterventionStatus (PENDING/IN_PROGRESS/RESOLVED)",
                    "notes : String (Proctor meeting notes)",
                    "created_at, resolved_at : LocalDateTime"
                },
                new String[]{"1:N Relationship with Student record"},
                new Color(220, 252, 231), new Color(22, 163, 74));

        drawClassBox(g, 480, 530, 480, 95, "User Accounts (In-Memory Credential Store)",
                new String[]{
                    "username : VARCHAR(50) [PRIMARY KEY]",
                    "password_hash : VARCHAR(64) [SHA-256 Digest]",
                    "role : UserRole (FACULTY / ADMIN / STUDENT)"
                },
                new String[]{},
                new Color(243, 232, 255), new Color(147, 51, 234));

        g.dispose();
        ImageIO.write(img, "png", target);
    }
}
