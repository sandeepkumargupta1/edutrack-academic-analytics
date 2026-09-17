# EduTrack: Academic Performance Analytics & Student Intervention Platform

> **VITyarthi Flipped Course Evaluated Project Submission**  
> **Course Title**: Object Oriented Programming using Java / Software Engineering  
> **Course Code**: CSE1007 / CSE2001  
> **Execution Mode**: 100% Pure Java SE Command-Line Interface (CLI)  
> **Repository Root URL**: `https://github.com/sandeepkumargupta1/edutrack-academic-analytics`

---

## 1. Project Title & Overview

**EduTrack** is an academic performance analytics and student intervention management system engineered in pure Java SE. It addresses the systemic problem of delayed academic failure detection in university continuous assessment environments. 

Rather than identifying failing students only after final examinations or semester detention lists are published, EduTrack continuously aggregates continuous assessment test scores (CAT-1, CAT-2), laboratory practicals, assignment submissions, quizzes, and biometric attendance logs. Using a multi-factor analytical scoring algorithm with non-linear sigmoid probability mapping, EduTrack computes failure risk, isolates underlying causal risk factors, and automatically generates personalized remedial action plans using the **GoF Strategy Pattern**.

---

## 2. Problem Statement

In higher education institutions:
1. **Late Failure Discovery**: Learning deficiencies and attendance shortages below the mandatory 75% statutory threshold are often identified only after semester examinations—when corrective measures are no longer possible.
2. **Unidimensional Evaluation**: Conventional grade books record raw scores without diagnosing root causes (e.g. chronic absenteeism vs. difficulty with problem-solving vs. backlog accumulation).
3. **Lack of Structured Intervention Tracking**: Faculty proctoring and remedial tutorials are frequently coordinated informally without auditable tracking of student progress.

---

## 3. Objectives

- Deliver a high-performance, modular Java CLI platform for continuous academic evaluation.
- Compute objective failure probability metrics ($P(\text{Fail}) \in [0.0, 1.0]$) and categorize students into `LOW`, `MODERATE`, and `HIGH` risk tiers.
- Identify specific causal drivers for at-risk students to enable explainable academic advising.
- Automatically generate personalized remedial interventions via the Gang-of-Four (GoF) Strategy Pattern.
- Provide a dual-mode CLI supporting both interactive faculty sessions and automated headless evaluation (`--report`) for grading pipelines.
- Ensure durable file-backed persistence using UTF-8 CSV storage with zero external database dependencies.

---

## 4. Scope

- **Included**: Demographic profiling, continuous assessment marks entry (IT1, IT2, Lab, Quiz, Assignment), attendance tracking, multi-factor risk computation, automated intervention generation, lifecycle status updates, ASCII visualizations, and CSV import/export.
- **Excluded**: Heavy desktop GUI frameworks (Swing/JavaFX) and external database server engines (MySQL/PostgreSQL), ensuring 100% terminal portability as mandated by the VITyarthi evaluation guidelines.

---

## 5. Target Users

- **Course Faculty & Instructors**: Enter assessment marks, inspect student performance, and assign remedial coursework.
- **Academic Counselors & Faculty Mentors (Proctors)**: Track attendance margins relative to the 75% threshold, review risk alerts, and record intervention notes.
- **Department Heads & Administrators**: Audit cohort-wide grade distribution histograms and evaluate intervention efficacy.
- **Enrolled Students**: Inspect personal continuous assessment records and assigned peer mentoring circles.

---

## 6. Functional Requirements & Major Modules

The system is organized into five major functional modules:

### Module 1: Authentication & Role-Based Access Control (RBAC)
- Secure credential verification matching SHA-256 password digests.
- Role capability segregation for `FacultyUser`, `AdminUser`, and `StudentUser`.

### Module 2: Student Profile & Continuous Assessment Management
- Full CRUD operations on student demographic records.
- Input validation on continuous assessment scores and attendance rates.
- File-backed CSV persistence with transactional state synchronization.

### Module 3: Computational Risk Assessment Engine
- Multi-factor risk calculation combining assessment deficits, attendance shortages, backlogs, and study hours.
- Sigmoid-style probabilistic mapping into `LOW`, `MODERATE`, and `HIGH` risk tiers.
- Automatic extraction of causal risk drivers (e.g., `Attendance shortage: 48.5% (< 75.0% statutory threshold)`).

### Module 4: Strategy-Driven Intervention Engine
- Dynamic instantiation of action plans using the GoF Strategy Pattern:
  - `RemedialClassStrategy`: Assigned for internal test marks $< 22/50$ or composite $< 50$.
  - `AttendanceCounselingStrategy`: Assigned for attendance $< 75.0\%$.
  - `PeerTutoringStrategy`: Assigned for backlogs or moderate risk status.
- State tracking (`PENDING` $\to$ `IN_PROGRESS` $\to$ `RESOLVED` / `ESCALATED`).

### Module 5: Terminal Visualization & Headless Audit Engine
- Bordered ASCII tables with auto-padding and ANSI syntax colors.
- ASCII horizontal bar charts for grade distributions and risk breakdowns.
- Non-interactive headless audit mode (`--report`) for automated CI/CD and grading pipelines.

👉 *For exhaustive specifications of all inputs, processing algorithms, outputs, and relevant classes, see [`docs/functional-requirements.md`](docs/functional-requirements.md).*

---

## 7. Non-Functional Requirements

- **Performance**: $O(1)$ in-memory lookups via `ConcurrentHashMap`; sub-15ms cohort risk evaluation across 1,000 records.
- **Security**: SHA-256 cryptographic password hashing; programmatic RBAC method guards.
- **Maintainability**: Strict 5-tier layered architecture separating CLI, Service, Strategy, Domain, and Repository layers.
- **Reliability & Error Handling**: Graceful recovery from malformed inputs and custom exception hierarchy (`StudentNotFoundException`, `DuplicateRecordException`, `InvalidAcademicRecordException`, `AuthenticationException`).
- **Usability**: Bordered ASCII tables, color-coded risk badges, and clear navigation prompts.
- **Resource Efficiency**: Zero external JAR dependencies; resident JVM heap footprint $\le 64\text{ MB}$.

👉 *For complete metrics, verification techniques, and code evidence, see [`docs/non-functional-requirements.md`](docs/non-functional-requirements.md).*

---

## 8. Key Features

- **Zero-Dependency Architecture**: Runs on standard Java SE (JDK 17+) without foreign frameworks.
- **Dual Execution Modes**: Interactive terminal menu and headless batch evaluation (`--report`).
- **Explainable Analytics**: Explains *why* a student is at risk, not just a score.
- **Automated Interventions**: Dynamically selects action plans using polymorphism.
- **Durable CSV Storage**: Working database (`students_db.csv`) auto-synced on all mutations.

---

## 9. OOP Concepts & Design Patterns Used

| Principle / Pattern | Concrete Implementation in EduTrack |
| :--- | :--- |
| **Encapsulation** | Strict private fields, validated setters, and domain invariant enforcement in `AcademicRecord`, `Student`, and `User`. |
| **Inheritance** | `User` abstract base class extended by `FacultyUser`, `StudentUser`, and `AdminUser`. |
| **Polymorphism** | Abstract method `getDashboardCapabilities()` returning role-specific permissions; `InterventionStrategy` executed polymorphically. |
| **Abstraction** | Interfaces `StudentRepository` and `InterventionStrategy` decoupling business logic from concrete storage and algorithms. |
| **Collections & Streams** | `ConcurrentHashMap` for $O(1)$ indexing; Java Stream API for filtering, sorting, and statistical reductions (`summaryStatistics`). |
| **GoF Strategy Pattern** | `InterventionStrategy` interface with concrete strategies `RemedialClassStrategy`, `AttendanceCounselingStrategy`, and `PeerTutoringStrategy`. |
| **Repository Pattern** | `StudentRepository` interface implemented by `InMemoryStudentRepository` and decorated by `FileStudentRepository`. |
| **Service-Layer Architecture** | Clean boundary between CLI presentation and domain services (`AuthService`, `StudentService`, `AnalyticsEngine`, `InterventionService`). |

---

## 10. Architecture & Diagrams

EduTrack follows a **5-Tier Layered Architecture**:

```
Presentation Layer (CLI) -> Service Layer -> Strategy Layer -> Domain Layer -> Persistence Layer (CSV)
```

Formal diagram assets are provided in the `docs/` directory:
- **System Architecture**: [`docs/architecture-diagram.png`](docs/architecture-diagram.png) ([Mermaid](docs/diagrams/architecture.mmd) / [PlantUML](docs/diagrams/architecture.puml))
- **Operational Workflow**: [`docs/workflow-diagram.png`](docs/workflow-diagram.png) ([Mermaid](docs/diagrams/workflow.mmd) / [PlantUML](docs/diagrams/workflow.puml))
- **UML Use Case**: [`docs/use-case-diagram.png`](docs/use-case-diagram.png) ([Mermaid](docs/diagrams/use-case.mmd) / [PlantUML](docs/diagrams/use-case.puml))
- **UML Class Diagram**: [`docs/class-diagram.png`](docs/class-diagram.png) ([Mermaid](docs/diagrams/class.mmd) / [PlantUML](docs/diagrams/class.puml))
- **UML Sequence Diagram**: [`docs/sequence-diagram.png`](docs/sequence-diagram.png) ([Mermaid](docs/diagrams/sequence.mmd) / [PlantUML](docs/diagrams/sequence.puml))
- **Storage / ER Schema**: [`docs/er-diagram.png`](docs/er-diagram.png) ([Mermaid](docs/diagrams/er.mmd) / [PlantUML](docs/diagrams/er.puml))

👉 *For detailed database specifications, field dictionary, in-memory collection topology, and dirty-write synchronization flow, see [`docs/storage-design.md`](docs/storage-design.md).*

---

## 11. Technologies & Tools

- **Language**: Java 17 / 21 / 26 (Standard Java SE)
- **Runtime**: Java Virtual Machine (JVM)
- **Compiler**: `javac` with UTF-8 encoding
- **Build Scripts**: Windows Batch (`compile.bat`, `run.bat`, `test.bat`) & Unix Shell (`compile.sh`, `run.sh`, `test.sh`)
- **Version Control**: Git (GitHub)
- **Persistence**: UTF-8 flat-file CSV storage with concurrent hash indexing

---

## 12. Project Structure

```
.
├── compile.bat / compile.sh       # One-click compile scripts
├── run.bat / run.sh               # One-click interactive CLI execution scripts
├── test.bat / test.sh             # One-click automated test runner scripts
├── pom.xml                        # Maven configuration
├── .gitignore                     # Git configuration
├── statement.md                   # Problem Statement & Scope
├── README.md                      # Evaluator Setup & Usage Guide
├── data/
│   ├── students_seed.csv          # Initial seed dataset (20 student profiles)
│   └── students_db.csv           # Persistent working database file
├── docs/
│   ├── README.md                  # Documentation and diagram index
│   ├── functional-requirements.md # Dedicated FR specification (FR-01 to FR-11)
│   ├── non-functional-requirements.md # Dedicated NFR specification (NFR-01 to NFR-06)
│   ├── storage-design.md          # Storage architecture & CSV data dictionary
│   ├── testing.md                 # Complete test report (26 tests, 30 runs, 100% pass)
│   ├── requirements.md            # Consolidated system requirements document
│   ├── report-outline.md          # 15-section project report structure
│   ├── vityarthi-compliance.md    # Formal compliance audit table
│   ├── design_artefacts.md        # Technical architecture and diagram source
│   ├── PROJECT_REPORT.md          # Complete project report in Markdown
│   ├── PROJECT_REPORT.pdf         # Compiled PDF project report
│   ├── architecture-diagram.png   # Generated architecture diagram
│   ├── workflow-diagram.png       # Generated workflow diagram
│   ├── use-case-diagram.png       # Generated use case diagram
│   ├── class-diagram.png          # Generated class diagram
│   ├── sequence-diagram.png       # Generated sequence diagram
│   ├── er-diagram.png             # Generated storage schema diagram
│   ├── diagrams/                  # Source Mermaid (.mmd) and PlantUML (.puml) files
│   └── screenshots/               # Terminal execution captures and guidance
│       └── README.md
├── src/
│   └── com/
│       └── edutrack/
│           ├── EduTrackApp.java   # Main CLI entrypoint
│           ├── model/             # Domain entities (User, Student, AcademicRecord, etc.)
│           ├── repository/        # DAO interfaces & file implementations
│           ├── service/           # Business services (Auth, Student, Analytics, Intervention)
│           ├── strategy/          # GoF Strategy pattern implementations
│           ├── exception/         # Custom exception hierarchy
│           ├── util/              # Table, ANSI color, CSV, and ASCII chart utilities
│           └── cli/               # CLI controller, menus, and input validators
└── test/
    └── com/
        └── edutrack/
            ├── TestRunner.java                        # Standalone unit test harness
            ├── service/
            │   ├── AuthServiceTest.java               # Auth & hash tests
            │   ├── StudentServiceTest.java            # Student CRUD tests
            │   ├── AnalyticsEngineTest.java           # Risk scoring tests
            │   ├── InterventionServiceTest.java       # Strategy pattern tests
            │   └── InputValidationAndSecurityTest.java# Validation & security tests
            └── util/
                ├── CsvHandlerTest.java                # CSV persistence tests
                └── DiagramImageRenderer.java          # Diagram PNG rendering utility
```

---

## 13. Installation & Setup Instructions

### Prerequisites
- Java Development Kit (JDK 17 or higher: JDK 17, 21, or 26).
- Standard terminal (Windows Command Prompt, PowerShell, macOS Terminal, or Linux Bash).

### 13.1 Clone the Repository
```bash
git clone https://github.com/sandeepkumargupta1/edutrack-academic-analytics.git
cd edutrack-academic-analytics
```

### 13.2 Compile the Code
**On Windows:**
```cmd
compile.bat
```

**On Linux / macOS:**
```bash
chmod +x compile.sh run.sh test.sh
./compile.sh
```

---

## 14. How to Run the Application

### 14.1 Interactive Terminal Mode
**On Windows:**
```cmd
run.bat
```

**On Linux / macOS:**
```bash
./run.sh
```

**Direct Java Command:**
```bash
java "-Dfile.encoding=UTF-8" -cp bin com.edutrack.EduTrackApp
```

#### Pre-Configured Test User Credentials:
| Role | Username | Password | Permitted Operations |
| :--- | :--- | :--- | :--- |
| **Faculty Member** | `faculty` | `admin123` | View directory, enter marks, run analytics, trigger interventions, export CSV |
| **Administrator** | `admin` | `root123` | Full administrative control, user registry inspection, cohort audit |
| **Student** | `student` | `student123` | Inspect personal academic records, attendance margin, assigned interventions |

---

### 14.2 Headless Automated Evaluation Mode (`--report`)
The automated evaluation pipeline can execute EduTrack in headless mode to verify student assessment calculations, risk algorithms, and intervention generation instantly without any manual input:

**On Windows:**
```cmd
run.bat --report
```

**On Linux / macOS:**
```bash
./run.sh --report
```

---

## 15. How to Test & Verification

EduTrack includes a self-contained automated unit test harness (`TestRunner`) that runs without external testing library JARs.

### Running the Test Suite:
**On Windows:**
```cmd
test.bat
```

**On Linux / macOS:**
```bash
./test.sh
```

### Test Coverage (26 Tests, 100% Pass Rate):
- **`AuthServiceTest` (5 tests)**: Password hashing consistency, valid login, invalid password rejection, unknown user rejection, role permissions.
- **`StudentServiceTest` (6 tests)**: Student creation, retrieval, duplicate rejection, academic record updates, searching, deletion.
- **`AnalyticsEngineTest` (5 tests)**: Low risk evaluation, high risk evaluation, attendance deficiency detection, cohort averages, grade histogram buckets.
- **`InterventionServiceTest` (3 tests)**: Remedial coaching strategy generation, attendance advisory strategy generation, lifecycle status transitions.
- **`InputValidationAndSecurityTest` (6 tests)**: Negative mark rejection, mark $>50$ rejection, attendance bounds (0–100%), negative backlogs/study hours rejection, role capability segregation, registration regex validation.
- **`CsvHandlerTest` (1 test)**: Bidirectional file serialization roundtrip.

👉 *For the complete 26-test verification log, defect resolutions, and 30-run stability audit, see [`docs/testing.md`](docs/testing.md).*

---

## 16. Sample Input and Output

### Sample Input:
- **Student Profile**: Reg No: `23BCE1025`, Name: `Rahul Sen`, Email: `rahul.sen2023@vitstudent.ac.in`, Branch: `CSE`, Semester: `5`, CGPA: `5.40`.
- **Continuous Assessment Marks**: Internal Test 1: `16.0 / 50`, Internal Test 2: `14.5 / 50`, Assignment: `8.5 / 20`, Lab: `12.0 / 30`, Quiz: `6.0 / 20`, Attendance: `52.0%`, Study Hours: `3.5 hrs/wk`, Backlogs: `3`.

### Sample Output:
- **Normalized Composite Score**: `34.1 / 100.0`
- **Risk Classification**: `HIGH RISK`
- **Failure Probability**: `95.4%` (via Sigmoid risk calculation)
- **Primary Risk Drivers**:
  - `Attendance shortage: 52.0% (< 75.0% statutory threshold)`
  - `Internal Test 1 deficit: 16.0/50 marks`
  - `Internal Test 2 deficit: 14.5/50 marks`
  - `3 active arrears/backlogs`
  - `Low self-study allocation: 3.5 hrs/week (< 6.0 hrs/week)`
- **Generated Interventions**:
  - `[INT-1022] Mandatory Remedial Classes in CSE2001 (Remedial Coaching & Doubt Clearing)`
  - `[INT-1023] Urgent Attendance Advisory & Parental Notification (Attendance Shortage Counseling)`
  - `[INT-1024] Peer Mentorship & Collaborative Learning Circle (Peer Tutoring & Study Group Pairing)`

---

## 17. Screenshots & Terminal Captures

Representative terminal outputs and capture instructions for evaluation are documented in:
👉 [`docs/screenshots/README.md`](docs/screenshots/README.md)

Outputs include:
1. System Login Screen with role credentials.
2. Main Navigation Menu.
3. Enrolled Student Directory Table.
4. Detailed Student Dossier & Risk Gauge.
5. Cohort Analytics & ASCII Grade Histograms.
6. Early Warning Risk Alert Roster.
7. Strategy-Driven Intervention Management.
8. Automated Unit Test Runner (26/26 Passed).

---

## 18. Challenges Faced

1. **Terminal Character Encoding & ANSI Formatting**: Windows Command Prompt historically defaulted to code page 437. Resolved by enforcing `-Dfile.encoding=UTF-8` and providing clean fallback padding in `TableRenderer`.
2. **Zero-Dependency Automated Testing**: Modern Java testing relies on JUnit 5 jars requiring Maven downloads. To eliminate build failures on offline evaluation systems, a custom reflection-based `TestRunner` was engineered into the codebase.
3. **Multi-Factor Risk Modeling**: Balancing attendance deficiency against test marks. Resolved by implementing an institutional threshold penalty that overrides raw scores when attendance falls below 60%.

---

## 19. Future Enhancements

1. **JDBC Database Connector**: Optional PostgreSQL/MySQL storage driver implementing `StudentRepository`.
2. **Automated Notification Service**: JavaMail API integration to dispatch automated email advisories to faculty proctors and parents.
3. **Multi-Semester Longitudinal Analytics**: Tracking historical score progression across multiple academic semesters.

---

## 20. References

1. Gamma, E., Helm, R., Johnson, R., & Vlissides, J. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*. Addison-Wesley.
2. Bloch, J. (2018). *Effective Java* (3rd ed.). Addison-Wesley Professional.
3. Oracle Corporation. (2024). *Java Platform, Standard Edition Documentation (Java SE 17 & 21)*.
4. Vellore Institute of Technology. *Academic Regulations and Examination Guidelines for Continuous Assessment (CAT-1, CAT-2, FAT)*.
