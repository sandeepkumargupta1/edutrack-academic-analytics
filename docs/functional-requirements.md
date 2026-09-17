# EduTrack Functional Requirements Specification

This document details the functional requirements of the **EduTrack** Academic Performance Analytics & Student Intervention Platform. Every requirement documented here directly corresponds to implemented, working Java classes in the codebase.

---

## Module 1: Authentication and Role-Based Access Control (RBAC)

### FR-01: User Credential Authentication
- **Requirement ID**: FR-01
- **Description**: Verifies user identity against stored credentials before permitting access to protected academic commands.
- **Input**: `username` (String), `password` (String).
- **Processing**:
  1. Retrieve user by username from `userRegistry` in `AuthService`.
  2. Compute SHA-256 cryptographic digest of the input password.
  3. Compare computed digest with `user.getPasswordHash()`.
  4. On match, bind the user instance as `currentUser`. On mismatch or missing user, throw `AuthenticationException`.
- **Output**: Authenticated `User` instance; updates session state in `AuthService`.
- **Relevant Classes/Modules**:
  - `com.edutrack.service.AuthService`
  - `com.edutrack.model.User`
  - `com.edutrack.exception.AuthenticationException`
  - `com.edutrack.cli.CliController`

### FR-02: Role-Based Capability Authorization
- **Requirement ID**: FR-02
- **Description**: Restricts access to sensitive academic management operations based on the authenticated user's role (`FACULTY`, `ADMIN`, or `STUDENT`).
- **Input**: Active `User` session, invoked menu command.
- **Processing**:
  1. Inspect `user.getRole()` and `user.getDashboardCapabilities()`.
  2. `StudentUser` is granted read-only access to self records (`VIEW_SELF_ACADEMICS`, `VIEW_SELF_ATTENDANCE`, `VIEW_SELF_INTERVENTIONS`).
  3. `FacultyUser` is granted mark updating, risk analysis, and intervention management capabilities (`VIEW_STUDENTS`, `UPDATE_MARKS`, `RUN_ANALYTICS`, `TRIGGER_INTERVENTIONS`, `EXPORT_REPORTS`).
  4. `AdminUser` is granted full administrative operations (`MANAGE_USERS`, `SYSTEM_MAINTENANCE`).
- **Output**: Permitted operational execution or authorization rejection.
- **Relevant Classes/Modules**:
  - `com.edutrack.model.UserRole`
  - `com.edutrack.model.User` (Abstract base)
  - `com.edutrack.model.FacultyUser`
  - `com.edutrack.model.StudentUser`
  - `com.edutrack.model.AdminUser`
  - `com.edutrack.cli.CliController`

---

## Module 2: Student and Academic Data Management

### FR-03: Student Profile Enrollment & Lookup
- **Requirement ID**: FR-03
- **Description**: Registers new student profiles with validated registration numbers and enables lookups by registration number or search terms.
- **Input**: `regNumber` (String, e.g. `23BCE1001`), `name` (String), `email` (String), `department` (String), `semester` (int: 1–10), `cgpa` (double: 0.0–10.0), `mentorName` (String).
- **Processing**:
  1. Validate registration format using regex `^[0-9]{2}[A-Za-z]{3}[0-9]{4}$`.
  2. Validate email syntax using regex `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$`.
  3. Verify registration number uniqueness; throw `DuplicateRecordException` if key exists.
  4. Instantiate `Student` entity and persist via `StudentRepository.save()`.
  5. Auto-flush changes to `data/students_db.csv`.
- **Output**: Persisted `Student` entity; rendered ASCII confirmation message.
- **Relevant Classes/Modules**:
  - `com.edutrack.model.Student`
  - `com.edutrack.repository.StudentRepository`
  - `com.edutrack.repository.FileStudentRepository`
  - `com.edutrack.service.StudentService`
  - `com.edutrack.cli.InputValidator`
  - `com.edutrack.exception.DuplicateRecordException`
  - `com.edutrack.exception.StudentNotFoundException`

### FR-04: Continuous Assessment Record Management
- **Requirement ID**: FR-04
- **Description**: Records, validates, and updates continuous assessment marks for a student across internal examination components.
- **Input**:
  - `internalTest1` (double: 0.0 to 50.0)
  - `internalTest2` (double: 0.0 to 50.0)
  - `assignmentScore` (double: 0.0 to 20.0)
  - `labScore` (double: 0.0 to 30.0)
  - `quizScore` (double: 0.0 to 20.0)
  - `studyHoursPerWeek` (double: $\ge 0.0$)
  - `backlogsCount` (int: $\ge 0$)
- **Processing**:
  1. Enforce strict range constraints in constructor; throw `IllegalArgumentException` on violation.
  2. Calculate normalized composite continuous mark out of 100:
     $$\text{Composite} = \left(\frac{\text{IT1}}{50} \times 25\right) + \left(\frac{\text{IT2}}{50} \times 25\right) + \left(\frac{\text{Asgn}}{20} \times 15\right) + \left(\frac{\text{Lab}}{30} \times 20\right) + \left(\frac{\text{Quiz}}{20} \times 15\right)$$
  3. Update `student.setAcademicRecord(record)` and persist.
- **Output**: Updated `AcademicRecord` attached to student; recalculated composite score.
- **Relevant Classes/Modules**:
  - `com.edutrack.model.AcademicRecord`
  - `com.edutrack.model.Student`
  - `com.edutrack.service.StudentService`
  - `com.edutrack.exception.InvalidAcademicRecordException`

### FR-05: Attendance Management & Threshold Deficit Monitoring
- **Requirement ID**: FR-05
- **Description**: Tracks student attendance percentages and flags violations of the statutory 75.0% institutional attendance threshold.
- **Input**: `attendancePercentage` (double: 0.0 to 100.0).
- **Processing**:
  1. Validate value is between 0.0 and 100.0.
  2. `AcademicRecord.isAttendanceDeficient()` evaluates if `attendancePercentage < 75.0`.
  3. Compute attendance deficiency penalty used by the risk scoring algorithm:
     $$\text{Penalty}_{\text{Att}} = (75.0 - \text{Attendance}) \times 1.2 \quad (\text{if Attendance} < 75.0)$$
- **Output**: Boolean deficiency status; ANSI color-coded attendance display (red for $<75\%$, green for $\ge 75\%$).
- **Relevant Classes/Modules**:
  - `com.edutrack.model.AcademicRecord`
  - `com.edutrack.util.AnsiColor`
  - `com.edutrack.cli.CliController`

---

## Module 3: Academic Analytics and Risk Analysis

### FR-06: Cohort-Wide Performance Aggregation
- **Requirement ID**: FR-06
- **Description**: Computes statistical summaries across all enrolled students in the cohort.
- **Input**: `List<Student>` retrieved from `StudentRepository`.
- **Processing**:
  1. Calculate cohort average CGPA: `students.stream().mapToDouble(Student::getCgpa).average()`.
  2. Calculate cohort average attendance: `mapToDouble(s -> s.getAcademicRecord().getAttendancePercentage()).average()`.
  3. Calculate cohort average composite marks: `mapToDouble(s -> s.getAcademicRecord().calculateCompositeScore()).average()`.
  4. Aggregate grade distribution buckets: S ($\ge 90$), A (80–89), B (70–79), C (60–69), D (50–59), F ($<50$).
- **Output**: Statistical indicators and ASCII horizontal bar chart grade distribution histograms.
- **Relevant Classes/Modules**:
  - `com.edutrack.service.AnalyticsEngine`
  - `com.edutrack.util.AsciiChartRenderer`
  - `com.edutrack.cli.CliController`

### FR-07: Algorithmic Multi-Factor Risk Assessment & Driver Attribution
- **Requirement ID**: FR-07
- **Description**: Evaluates academic risk using a deterministic, multi-factor scoring formula and non-linear sigmoid probability transformation without external machine learning dependencies.
- **Input**: `Student` entity with associated `AcademicRecord` and `cgpa`.
- **Processing**:
  1. Compute score deficiency: $\text{Deficiency} = \max(0, (100.0 - \text{Composite}) \times 0.5)$.
  2. Compute attendance penalty: $\text{Penalty}_{\text{Att}} = (75.0 - \text{Att}) \times 1.2$ if $\text{Att} < 75.0$.
  3. Compute backlog penalty: $\text{Penalty}_{\text{Backlogs}} = \min(25.0, \text{Backlogs} \times 6.0)$.
  4. Compute study hours deficit: $\text{Deficit}_{\text{Hours}} = (7.0 - \text{Hours}) \times 1.5$ if $\text{Hours} < 7.0$.
  5. Calculate Total Risk Index $\in [0.0, 100.0]$.
  6. Map to Failure Probability via Sigmoid function:
     $$P(\text{Fail}) = \frac{1}{1 + e^{-0.08 \times (\text{TotalRiskIndex} - 45.0)}}$$
  7. Assign `RiskLevel`:
     - `HIGH`: $P(\text{Fail}) \ge 0.60$ or $(\text{Att} < 60.0\% \land \text{Composite} < 45.0)$
     - `MODERATE`: $P(\text{Fail}) \ge 0.35$ or $\text{Att} < 75.0\%$ or $\text{Backlogs} > 0$
     - `LOW`: $P(\text{Fail}) < 0.35$
  8. Extract explicit, human-readable causal risk factors.
- **Output**: `RiskAssessment` object containing `RiskLevel`, `failureProbability`, `projectedFinalScore`, `keyRiskFactors`, and `recommendation`.
- **Relevant Classes/Modules**:
  - `com.edutrack.service.AnalyticsEngine`
  - `com.edutrack.model.RiskAssessment`
  - `com.edutrack.model.RiskLevel`
  - `com.edutrack.util.AsciiChartRenderer`

---

## Module 4: Intervention and Recommendation Generation

### FR-08: Strategy-Driven Automated Intervention Instantiation
- **Requirement ID**: FR-08
- **Description**: Dynamically generates personalized remedial action plans based on specific diagnostic deficits using the GoF Strategy Pattern.
- **Input**: `Student` entity with completed `AcademicRecord` and `RiskAssessment`.
- **Processing**:
  1. Query registered strategies in `InterventionService`:
     - `RemedialClassStrategy`: Triggers if $\text{IT1} < 22.0$ or $\text{IT2} < 22.0$ or $\text{Composite} < 50.0$.
     - `AttendanceCounselingStrategy`: Triggers if $\text{Attendance} < 75.0\%$.
     - `PeerTutoringStrategy`: Triggers if student is Moderate Risk, has backlogs $> 0$, or study hours $< 8.0$.
  2. Avoid duplicate active interventions for identical strategies.
  3. Generate unique intervention ID (`INT-1001`, `INT-1002`, ...).
  4. Attach generated interventions to `student.addIntervention()`.
- **Output**: List of instantiated `Intervention` entities with assigned mentors, titles, and remedial instructions.
- **Relevant Classes/Modules**:
  - `com.edutrack.service.InterventionService`
  - `com.edutrack.strategy.InterventionStrategy` (Interface)
  - `com.edutrack.strategy.RemedialClassStrategy`
  - `com.edutrack.strategy.AttendanceCounselingStrategy`
  - `com.edutrack.strategy.PeerTutoringStrategy`
  - `com.edutrack.model.Intervention`

### FR-09: Intervention Lifecycle Tracking & Notes Recording
- **Requirement ID**: FR-09
- **Description**: Tracks intervention progression through state transitions and records faculty consultation notes.
- **Input**: `interventionId` (String), `newStatus` (`PENDING`, `IN_PROGRESS`, `RESOLVED`, `ESCALATED`), `notes` (String).
- **Processing**:
  1. Lookup intervention by ID in `interventionRegistry`.
  2. Update status; if status is `RESOLVED`, record `resolvedAt` timestamp.
  3. Append consultation/meeting notes to `intervention.notes`.
- **Output**: Boolean success flag; updated `Intervention` record.
- **Relevant Classes/Modules**:
  - `com.edutrack.service.InterventionService`
  - `com.edutrack.model.Intervention`
  - `com.edutrack.model.InterventionStatus`
  - `com.edutrack.cli.CliController`

---

## Module 5: Persistence, Reporting & Headless Evaluation

### FR-10: File-Backed CSV Persistence & Export
- **Requirement ID**: FR-10
- **Description**: Synchronizes student demographic and academic records to disk in UTF-8 CSV format and exports timestamped reports.
- **Input**: `File` handle to CSV storage path (`data/students_db.csv`).
- **Processing**:
  1. Load existing records on startup; fallback to seed file (`data/students_seed.csv`) if database file is empty.
  2. Flush modified in-memory records to disk on student creation, marks update, or deletion.
  3. Export formatted CSV snapshots to `data/export/edutrack_export_<timestamp>.csv`.
- **Output**: Synchronized CSV file on disk; exported report file.
- **Relevant Classes/Modules**:
  - `com.edutrack.repository.FileStudentRepository`
  - `com.edutrack.repository.InMemoryStudentRepository`
  - `com.edutrack.util.CsvHandler`

### FR-11: Headless Automated Evaluation Mode (`--report`)
- **Requirement ID**: FR-11
- **Description**: Executes cohort analytics, risk detection, and intervention auditing without interactive prompts when invoked with command-line flags (`--report`, `--batch`, `-r`).
- **Input**: Command-line arguments array `args`.
- **Processing**:
  1. Detect `--report` flag in `EduTrackApp.main()`.
  2. Delegate execution to `CliController.executeHeadlessReport()`.
  3. Load data, run cohort analytics, evaluate risk, generate interventions, and stream formatted ASCII tables and histograms to stdout.
  4. Exit with return code `0`.
- **Output**: Terminal audit stream to standard output; exit code `0`.
- **Relevant Classes/Modules**:
  - `com.edutrack.EduTrackApp`
  - `com.edutrack.cli.CliController`
