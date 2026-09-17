# EduTrack Software Requirements Specification (SRS)

This document formalizes the functional and non-functional requirements implemented in the **EduTrack** Academic Performance Analytics & Student Intervention Platform. All requirements reflect capabilities actively supported in the Java codebase.

---

## 1. Functional Requirements (FR)

### FR-01: User Authentication
- **Description**: The system must authenticate users via unique username and password credentials before granting access to application features.
- **Input**: Username string, plaintext password string.
- **Processing**: Compute SHA-256 digest of input password and match against registered user credentials in `AuthService`.
- **Output**: Authenticated user session or `AuthenticationException` with descriptive error message.

### FR-02: Role-Based Access Control (RBAC)
- **Description**: The system must segregate capabilities based on explicit user roles: `FACULTY`, `ADMIN`, and `STUDENT`.
- **Processing**: `User.getDashboardCapabilities()` provides role-specific permissions. Students can only view self records; faculty can enter marks, run analytics, and trigger interventions; administrators can manage profiles and system functions.
- **Output**: Selective menu rendering and enforcement of operational boundaries.

### FR-03: Student Profile Management (CRUD)
- **Description**: The system must manage student demographic profiles.
- **Input**: Registration number (e.g. `23BCE1001`), full name, institutional email, academic department, semester (1–10), CGPA (0.0–10.0), and faculty proctor name.
- **Processing**: Validate registration number format and email regex; enforce unique primary key constraints via `StudentRepository`.
- **Output**: Created, updated, or retrieved `Student` entity; throws `DuplicateRecordException` on key collision or `StudentNotFoundException` when record is absent.

### FR-04: Academic Record & Continuous Assessment Tracking
- **Description**: The system must record and update continuous assessment component marks for enrolled courses.
- **Input**: Course code, Internal Test 1 (0.0–50.0), Internal Test 2 (0.0–50.0), Assignment (0.0–20.0), Lab Practical (0.0–30.0), Quiz (0.0–20.0), Weekly study hours ($\ge 0.0$), and Active backlogs count ($\ge 0$).
- **Processing**: Validate score boundaries; calculate normalized 100-point composite score:
  $$\text{Composite} = \left(\frac{\text{IT1}}{50} \times 25\right) + \left(\frac{\text{IT2}}{50} \times 25\right) + \left(\frac{\text{Asgn}}{20} \times 15\right) + \left(\frac{\text{Lab}}{30} \times 20\right) + \left(\frac{\text{Quiz}}{20} \times 15\right)$$
- **Output**: Persisted `AcademicRecord` attached to student entity.

### FR-05: Attendance Deficit Monitoring
- **Description**: The system must track biometric attendance percentages and flag violations of the institutional statutory 75.0% threshold.
- **Input**: Attendance percentage (0.0–100.0%).
- **Processing**: `AcademicRecord.isAttendanceDeficient()` evaluates if attendance $< 75.0\%$. Computes attendance deficit penalty:
  $$\text{Penalty}_{\text{Att}} = (75.0 - \text{Attendance}) \times 1.2 \quad (\text{if Attendance} < 75.0)$$
- **Output**: Terminal highlight (red text if deficient, green if compliant) and causal risk factor attribution.

### FR-06: Cohort Analytics & Statistical Summary
- **Description**: The system must aggregate academic indicators across all enrolled students in the cohort.
- **Processing**: Use Java Streams to calculate:
  - Cohort Average CGPA: $\frac{1}{N}\sum \text{CGPA}$
  - Cohort Average Attendance: $\frac{1}{N}\sum \text{Attendance}$
  - Cohort Average Continuous Composite Mark: $\frac{1}{N}\sum \text{Composite}$
  - Grade distribution histogram across standard university brackets (S: $\ge 90$, A: 80–89, B: 70–79, C: 60–69, D: 50–59, F: $<50$).
- **Output**: Summary metrics table and ASCII horizontal bar chart histograms rendered via `AsciiChartRenderer`.

### FR-07: Computational Academic Risk Analysis & Attribution
- **Description**: The system must quantitatively assess student failure probability and categorize students into distinct risk tiers without requiring external machine learning libraries.
- **Processing**: `AnalyticsEngine.assessStudentRisk()` computes total multi-factor risk index ($0.0 - 100.0$) combining continuous score deficit, attendance shortage penalty, backlog arrears, and self-study deficiency. Computes probabilistic failure score via Sigmoid curve:
  $$P(\text{Fail}) = \frac{1}{1 + e^{-0.08 \times (\text{RiskIndex} - 45.0)}}$$
  - $P(\text{Fail}) \ge 0.60 \implies$ `HIGH RISK`
  - $P(\text{Fail}) \ge 0.35 \implies$ `MODERATE RISK`
  - $P(\text{Fail}) < 0.35 \implies$ `LOW RISK`
- **Output**: `RiskAssessment` object containing risk level, failure probability percentage, projected final score, and specific human-readable causal risk drivers.

### FR-08: Automated Intervention Generation via Strategy Pattern
- **Description**: The system must automatically select and instantiate tailored academic intervention plans based on student diagnostic deficits.
- **Processing**: `InterventionService` evaluates registered `InterventionStrategy` implementations:
  - `RemedialClassStrategy`: Triggered if Internal Test 1 or 2 $< 22.0/50$ or Composite $< 50.0$.
  - `AttendanceCounselingStrategy`: Triggered if Attendance $< 75.0\%$.
  - `PeerTutoringStrategy`: Triggered if student is classified Moderate Risk or has active backlogs.
- **Output**: Actionable `Intervention` entities bound to the student, containing unique intervention IDs (e.g. `INT-1004`), titles, mentor assignments, and tailored remedial instructions.

### FR-09: Intervention Lifecycle & Action Tracking
- **Description**: The system must track the state transitions and meeting notes for all assigned interventions.
- **Processing**: Faculty proctors update status from `PENDING` $\to$ `IN_PROGRESS` $\to$ `RESOLVED` or `ESCALATED`, appending follow-up consultation notes.
- **Output**: Filterable intervention audit roster with status color indicators.

### FR-10: CSV Data Ingestion & Durable Persistence
- **Description**: The system must persist records to UTF-8 flat-file CSV storage (`students_db.csv`) and support loading from initial seed datasets (`students_seed.csv`).
- **Processing**: `FileStudentRepository` reads records on startup into an in-memory `ConcurrentHashMap` and automatically flushes mutations to disk.
- **Output**: Synchronized disk storage with automatic recovery across sessions.

### FR-11: Headless Automated Evaluation Mode (`--report`)
- **Description**: The system must execute complete cohort auditing without user interaction when launched with the `--report`, `--batch`, or `-r` command-line argument.
- **Processing**: Loads data, executes cohort analytics, calculates risk ratings, generates interventions, prints ASCII tables/charts to stdout, and exits with code 0.
- **Output**: Complete non-interactive terminal audit log suitable for CI/CD and automated grading pipelines.

---

## 2. Non-Functional Requirements (NFR)

The system specifies five realistic, testable non-functional engineering standards:

### NFR-01: Performance & Response Time
- **Requirement**: Individual student lookup operations must execute in sub-millisecond time ($O(1)$ complexity) using hash indexing.
- **Evaluation Metric**: Cohort analytics, multi-factor risk calculations, and intervention evaluations across 1,000 student records must complete in under 50 milliseconds on standard hardware without UI freezing.
- **Verification**: Validated by automated test suite running 26 comprehensive assertions in under 2 seconds.

### NFR-02: Security & Credential Protection
- **Requirement**: Plaintext passwords must never be stored on disk or in memory.
- **Evaluation Metric**: Passwords must be cryptographically hashed using SHA-256 message digests with byte-array hex conversion.
- **Authorization Guard**: Unauthorized roles (such as students) must be programmatically prevented from accessing administrative or mark-updating methods.
- **Verification**: Validated by `AuthServiceTest` and `InputValidationAndSecurityTest`.

### NFR-03: Maintainability & Architectural Modularity
- **Requirement**: The codebase must adhere to strict layered architecture and separation of concerns.
- **Evaluation Metric**: Presentation logic (`cli`), business services (`service`), domain models (`model`), behavioral strategies (`strategy`), and persistence (`repository`) must reside in isolated packages. Adding a new intervention policy must require only a new `InterventionStrategy` class without modifying existing service code (Open-Closed Principle).
- **Verification**: Code review and package dependency analysis.

### NFR-04: Reliability & Input Fault Tolerance
- **Requirement**: The application must never terminate abruptly due to malformed user input, out-of-bounds numbers, or missing files.
- **Evaluation Metric**: Invalid inputs (such as text entered for marks, negative numbers, scores $>50$, attendance $>100\%$) must be caught, rejected with descriptive error messages, and prompt the user cleanly.
- **Verification**: Validated by `InputValidationAndSecurityTest` (covering negative scores, excessive marks, attendance bounds, and invalid regexes).

### NFR-05: Usability & Terminal Ergonomics
- **Requirement**: The CLI must provide intuitive navigation, readable data displays, and visual cues.
- **Evaluation Metric**: All data tables must feature dynamic column width calculation with borders (`TableRenderer`). Status indicators must use standard ANSI color codes (Green for pass/stable, Yellow for moderate warning, Red for high risk/error). ASCII histograms must render clear comparative proportions without requiring external plotting libraries.
- **Verification**: Terminal output inspection across standard 80x24 and wide terminal viewports.
