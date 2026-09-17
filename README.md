# EduTrack: Academic Performance Analytics & Student Intervention Platform

> **VITyarthi Flipped Course Evaluated Project Submission**  
> **Course**: Object Oriented Programming using Java (CSE1007 / CSE2001) / Software Engineering  
> **Platform**: Command-Line Interface (CLI) Pure Java SE System  
> **Repository Root URL**: `https://github.com/sandeepkumargupta1/edutrack-academic-analytics`

---

## 1. Project Overview

**EduTrack** is a modular, high-performance Java Command-Line Interface (CLI) system designed to solve the critical problem of late academic failure detection in higher education institutions. Rather than discovering academic failure and attendance shortages only after final semester examinations, EduTrack continuously aggregates continuous assessment test scores (CAT-1, CAT-2), lab marks, quizzes, weekly study hours, and biometric attendance.

Using a multi-factor risk assessment algorithm, EduTrack computes failure probabilities ($P(\text{Fail}) \in [0.0, 1.0]$), pinpoints explicit causal risk drivers (such as attendance deficiency, low internal scores, or backlog accumulation), and automatically triggers personalized academic interventions using the **GoF Strategy Pattern** (`RemedialClassStrategy`, `AttendanceCounselingStrategy`, `PeerTutoringStrategy`).

---

## 2. Key Features

- **Role-Based Access Control (RBAC)**: Secure authentication with SHA-256 hashed passwords separating `Faculty`, `Administrator`, and `Student` capabilities.
- **Continuous Academic Assessment Processing**: Complete CRUD operations for student profiles, course scores, continuous internals, and attendance metrics.
- **Explainable Predictive Risk Engine**: Quantitative risk modeling that classifies students into `LOW`, `MODERATE`, and `HIGH` risk tiers while generating human-interpretable causal risk driver summaries.
- **Automated Academic Intervention Engine**: Uses the Gang-of-Four (GoF) Strategy Pattern to automatically instantiate remedial sessions, attendance advisories, or peer tutoring circles based on student diagnostics.
- **Interactive Terminal UI with ASCII Visualizations**:
  - Bordered, ANSI-colored data tables.
  - Risk meter gauges (`[██████████░░░░░░░░░░] 48.0%`).
  - Terminal histograms for continuous assessment grade distributions and cohort risk breakdowns.
- **Dual Execution Modes**:
  - **Interactive Mode**: Guided terminal menu for interactive management and live score entry.
  - **Headless Evaluation Mode (`--report` / `--batch`)**: Non-interactive command-line execution ideal for automated grading pipelines, compiling and outputting complete analytical audits with exit code 0.
- **Zero-External-Dependency Runtime**: Built purely on standard Java SE (JDK 17/21/26). Does not require third-party GUI installations or external server setups.
- **Robust Built-In Automated Test Suite**: Comprehensive unit testing framework verifying models, services, analytics, and CSV persistence with 100% pass rates.

---

## 3. Technologies & Tools Used

- **Language**: Java 17 / 21 / 26 (Standard Java SE)
- **Paradigm**: Object-Oriented Programming (Encapsulation, Inheritance, Polymorphism, Abstraction, Strategy Pattern, Repository Pattern)
- **Data Structures**: Java Collections Framework (`ConcurrentHashMap`, `ArrayList`, `TreeSet`, `Queue`) and Stream API (`filter`, `map`, `sorted`, `summaryStatistics`)
- **Persistence**: File-backed CSV repository (`FileStudentRepository`, `CsvHandler`) with automatic transactional state syncing
- **Security**: Cryptographic password hashing (SHA-256 with byte array salt conversion)
- **Testing**: Built-in automated unit test runner (`com.edutrack.TestRunner`)
- **Build / Packaging**: Standard `javac` compilation scripts (`compile.bat`, `compile.sh`) and optional Maven `pom.xml`

---

## 4. Steps to Install & Run the Project

### Prerequisites
- Java Development Kit (JDK 17 or higher: JDK 17, 21, or 26).
- Terminal environment (Windows Command Prompt / PowerShell, macOS Terminal, or Linux Bash).

### 4.1 Clone the Repository
```bash
git clone https://github.com/sandeepkumargupta1/edutrack-academic-analytics.git
cd edutrack-academic-analytics
```

### 4.2 Compile the Code
EduTrack provides one-click scripts for compilation without requiring Maven:

**On Windows (Command Prompt / PowerShell):**
```cmd
compile.bat
```

**On Linux / macOS:**
```bash
chmod +x compile.sh run.sh test.sh
./compile.sh
```

*(Optional: If you prefer building with Maven, execute `mvn clean compile`)*

---

### 4.3 Run the Application (Command-Line)

#### Option A: Interactive Terminal Mode
Launch the interactive CLI dashboard:

**On Windows:**
```cmd
run.bat
```

**On Linux / macOS:**
```bash
./run.sh
```

**Or using direct Java invocation:**
```bash
java "-Dfile.encoding=UTF-8" -cp bin com.edutrack.EduTrackApp
```

#### Default Test Login Credentials:
| Role | Username | Password | Access Capabilities |
| :--- | :--- | :--- | :--- |
| **Faculty** | `faculty` | `admin123` | View roster, update marks, run analytics, trigger interventions, export CSV |
| **Administrator** | `admin` | `root123` | Full system access, user registry management, cohort audit |
| **Student** | `student` | `student123` | View personal dossier, attendance status, assigned interventions |

---

#### Option B: Non-Interactive Headless Evaluation Mode (`--report`)
The automated evaluation pipeline can execute EduTrack in headless mode to verify student assessment calculations, risk algorithms, and intervention generation instantly without any manual input:

**On Windows:**
```cmd
run.bat --report
```

**On Linux / macOS:**
```bash
./run.sh --report
```

**Or direct command:**
```bash
java "-Dfile.encoding=UTF-8" -cp bin com.edutrack.EduTrackApp --report
```

---

## 5. Instructions for Testing

EduTrack includes a self-contained automated unit test suite (`TestRunner`) that validates domain rules, calculations, duplicate constraints, and CSV round-trips.

### Running the Automated Test Suite:

**On Windows:**
```cmd
test.bat
```

**On Linux / macOS:**
```bash
./test.sh
```

**Or direct command:**
```bash
java "-Dfile.encoding=UTF-8" -cp bin com.edutrack.TestRunner
```

### Expected Test Output:
```
================================================================================
                   EDUTRACK AUTOMATED UNIT TEST RUNNER
================================================================================

Running: AuthServiceTest
  ✔ testInvalidPasswordRejection                  [PASSED]
  ✔ testPasswordHashingConsistency                [PASSED]
  ✔ testUnknownUserRejection                      [PASSED]
  ✔ testRoleAuthorization                         [PASSED]
  ✔ testValidFacultyLogin                         [PASSED]

Running: StudentServiceTest
  ? testDuplicateRegistrationRejection            [PASSED]
  ✔ testCreateAndRetrieveStudent                  [PASSED]
  ✔ testNonExistentStudentThrowsException         [PASSED]
  ✔ testUpdateAcademicRecord                      [PASSED]
  ✔ testSearchStudents                            [PASSED]
  ✔ testDeleteStudent                             [PASSED]

Running: AnalyticsEngineTest
  ✔ testCohortAveragesCalculation                 [PASSED]
  ✔ testGradeDistributionBuckets                  [PASSED]
  ✔ testHighRiskStudentAssessment                 [PASSED]
  ✔ testLowRiskStudentAssessment                  [PASSED]
  ✔ testAttendanceDeficiencyDetection             [PASSED]

Running: InterventionServiceTest
  ✔ testUpdateInterventionStatus                  [PASSED]
  ✔ testAttendanceInterventionGeneratedForDeficientAttendance [PASSED]
  ✔ testRemedialInterventionGeneratedForLowInternals [PASSED]

Running: CsvHandlerTest
  ✔ testSaveAndLoadRoundtrip                      [PASSED]

================================================================================
 TOTAL TESTS: 20 | PASSED: 20 | FAILED: 0
================================================================================
ALL TESTS PASSED SUCCESSFULLY! (100% Pass Rate)
```

---

## 6. Terminal Execution Preview

### 6.1 Enrolled Student Directory & Risk Assessment
```
+-----------+---------------------+--------------------------------+-----+------+------------+---------------+------------+
|  Reg No   |        Name         |           Department           | Sem | CGPA | Attendance | Composite/100 | Risk Level |
+-----------+---------------------+--------------------------------+-----+------+------------+---------------+------------+
| 23BCE1001 | Aarav Sharma        | Computer Science & Engineering | 5   | 8.85 | 94.5%      | 92.0          | LOW        |
| 23BCE1003 | Rohan Verma         | Computer Science & Engineering | 5   | 6.40 | 62.0%      | 47.0          | HIGH       |
| 23BCE1005 | Vikram Malhotra     | Computer Science & Engineering | 5   | 5.75 | 54.0%      | 36.3          | HIGH       |
| 23BCE1007 | Kabir Mehta         | Information Technology         | 5   | 6.80 | 72.0%      | 57.8          | MODERATE   |
| 23BCE1009 | Aditya Deshmukh     | Computer Science & Engineering | 5   | 5.20 | 48.5%      | 31.4          | HIGH       |
+-----------+---------------------+--------------------------------+-----+------+------------+---------------+------------+
```

### 6.2 Cohort Analytics & ASCII Distribution Histograms
```
COHORT ACADEMIC RISK BREAKDOWN
--------------------------------------------------
 High Risk (Urgent) | █████████████ 5
 Moderate Risk      | ████████ 3
 Low Risk (Stable)  | ██████████████████████████████ 12
--------------------------------------------------
CONTINUOUS ASSESSMENT GRADE HISTOGRAM
--------------------------------------------------
 S (>=90)  | ████████████████████████ 4
 A (80-89) | ████████████ 2
 B (70-79) | ████████████████████████ 4
 C (60-69) | ██████████████████ 3
 D (50-59) | ████████████ 2
 F (<50)   | ██████████████████████████████ 5
--------------------------------------------------
```

---

## 7. Project Structure

```
.
├── compile.bat / compile.sh       # One-click compile scripts
├── run.bat / run.sh               # One-click CLI launch scripts
├── test.bat / test.sh             # One-click automated test runner
├── pom.xml                        # Standard Maven configuration
├── .gitignore                     # Java Git configuration
├── statement.md                   # Problem Statement & Scope (Section 5.2)
├── README.md                      # Evaluator Setup & Usage Guide (Section 5.1)
├── data/
│   ├── students_seed.csv          # Initial seed dataset with 20 student profiles
│   └── students_db.csv           # Persistent working database file
├── docs/
│   ├── design_artefacts.md        # Architecture, Workflows, UML Class, Sequence, ER
│   └── PROJECT_REPORT.md          # Complete 15-section project report (Section 6)
├── src/
│   └── com/
│       └── edutrack/
│           ├── EduTrackApp.java   # Main CLI entrypoint
│           ├── model/             # Domain entities (User, Student, AcademicRecord, etc.)
│           ├── repository/        # DAO interfaces & file implementations
│           ├── service/           # Business services (Auth, Student, Analytics, Intervention)
│           ├── strategy/          # GoF Strategy pattern implementations
│           ├── exception/         # Custom exception hierarchy
│           ├── util/              # Terminal formatting, ANSI, CSV, ASCII charts
│           └── cli/               # CLI controller, menus, input validators
└── test/
    └── com/
        └── edutrack/
            ├── TestRunner.java    # Automated test execution engine
            ├── AuthServiceTest.java
            ├── StudentServiceTest.java
            ├── AnalyticsEngineTest.java
            ├── InterventionServiceTest.java
            └── CsvHandlerTest.java
```

---

## 8. Official Submission Guidelines Compliance

- **Course Relevance**: Directly implements concepts from **Object Oriented Programming using Java (CSE1007)** (Inheritance, Polymorphism, Encapsulation, Abstraction, Strategy Pattern, Custom Exceptions, Collections, Streams, File I/O).
- **Executable via Terminal**: Submissions requiring GUI setups are penalized. EduTrack is 100% executable from any terminal without GUI components.
- **Repository Visibility**: Repository must be set to **Public** before portal submission.
- **Submission URL Format**: Strict root URL: `https://github.com/sandeepkumargupta1/edutrack-academic-analytics` (Never submit a `/tree/main/` URL).
