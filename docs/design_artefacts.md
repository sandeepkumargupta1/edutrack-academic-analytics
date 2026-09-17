# EduTrack: System Design & Architectural Artefacts

This document provides formal architectural models and UML design diagrams for the **EduTrack** Academic Performance Analytics and Student Intervention System, satisfying Section 4 of the VITyarthi project submission requirements.

---

## 1. System Architecture Diagram

EduTrack adheres to a strict **Layered Architecture** with distinct separation of concerns:

```mermaid
graph TD
    subgraph Presentation Layer [Presentation Layer: CLI & Terminal UI]
        CLI[CliController]
        MENU[MenuOption & InputValidator]
        RENDER[TableRenderer & AsciiChartRenderer]
        CLI --> MENU
        CLI --> RENDER
    end

    subgraph Service Layer [Service / Business Logic Layer]
        AUTH[AuthService]
        STUDENT_SVC[StudentService]
        ANALYTICS[AnalyticsEngine]
        INTERVENTION_SVC[InterventionService]
    end

    subgraph Strategy Layer [Behavioral Strategy Layer]
        STRAT[InterventionStrategy Interface]
        REM[RemedialClassStrategy]
        ATT[AttendanceCounselingStrategy]
        PEER[PeerTutoringStrategy]
        STRAT --> REM
        STRAT --> ATT
        STRAT --> PEER
    end

    subgraph Domain Layer [Domain Models & Entities]
        USER[User / FacultyUser / StudentUser / AdminUser]
        STUDENT[Student Aggregate]
        RECORD[AcademicRecord]
        ASSESSMENT[RiskAssessment]
        INTERVENT[Intervention]
    end

    subgraph Persistence Layer [Data Access & Persistence Layer]
        REPO[StudentRepository Interface]
        INMEM[InMemoryStudentRepository]
        FILE_REPO[FileStudentRepository]
        CSV[CsvHandler Utility]
        STORAGE[(students_db.csv / students_seed.csv)]

        REPO --> INMEM
        INMEM --> FILE_REPO
        FILE_REPO --> CSV
        CSV --> STORAGE
    end

    CLI --> AUTH
    CLI --> STUDENT_SVC
    CLI --> ANALYTICS
    CLI --> INTERVENTION_SVC

    STUDENT_SVC --> REPO
    INTERVENTION_SVC --> STRAT
    INTERVENTION_SVC --> Domain Layer
    ANALYTICS --> Domain Layer
    STUDENT_SVC --> Domain Layer
```

---

## 2. Process Flow / Workflow Diagram

Illustrates the end-to-end operational flow from user authentication through continuous assessment evaluation, automated risk detection, and intervention resolution:

```mermaid
flowchart TD
    START([System Startup]) --> CHECK_MODE{CLI Mode?}
    CHECK_MODE -- "--report / --batch" --> HEADLESS[Execute Headless Audit & Export]
    HEADLESS --> TERMINATE([Exit Code 0])

    CHECK_MODE -- "Interactive" --> LOGIN_SCREEN[/Prompt User Credentials/]
    LOGIN_SCREEN --> AUTH_VAL{Verify SHA-256 Hash}
    AUTH_VAL -- Failure --> AUTH_ERR[Display Error & Retry]
    AUTH_ERR --> LOGIN_SCREEN

    AUTH_VAL -- Success --> MENU_LOOP[/Display Role-Based Menu/]
    
    MENU_LOOP --> OPT_ROSTER[1. View Roster] --> RENDER_TBL[Render ASCII Table] --> MENU_LOOP
    MENU_LOOP --> OPT_SEARCH[2. Search Student] --> DO_SEARCH[Filter by Keyword] --> MENU_LOOP
    MENU_LOOP --> OPT_PROFILE[3. Detailed Profile] --> DO_PROFILE[Show Scores, Risk Drivers, History] --> MENU_LOOP
    MENU_LOOP --> OPT_NEW[4. Register Student] --> VAL_REG[Validate Input Regex & Unique ID] --> SAVE_STUDENT[Persist to DB] --> MENU_LOOP
    MENU_LOOP --> OPT_MARKS[5. Update Marks] --> CALC_COMP[Recalculate Composite Score & Att.] --> RUN_RISK[Assess Risk Index] --> MENU_LOOP
    MENU_LOOP --> OPT_ANALYTICS[6. Cohort Analytics] --> RENDER_CHARTS[Render Histograms & KPI Cards] --> MENU_LOOP
    MENU_LOOP --> OPT_ALERT[7. Early Risk Detection] --> FILTER_RISK[Filter High & Moderate Risk Students] --> MENU_LOOP
    MENU_LOOP --> OPT_INTERVENT[8. Trigger Interventions] --> EVAL_STRAT[Evaluate Strategy Conditions] --> GEN_PLANS[Create Action Plans] --> MENU_LOOP
    MENU_LOOP --> OPT_MANAGE[9. Manage Interventions] --> UPDATE_STATUS[Set Status: In Progress / Resolved] --> MENU_LOOP
    MENU_LOOP --> OPT_EXPORT[10. Export CSV] --> WRITE_FILE[Write Export File to Disk] --> MENU_LOOP
    MENU_LOOP --> OPT_LOGOUT[11. Logout] --> LOGIN_SCREEN
    MENU_LOOP --> OPT_EXIT[0. Exit] --> TERMINATE
```

---

## 3. UML Use Case Diagram

Defines interactions between system actors (Faculty, Administrator, Student) and primary use cases:

```mermaid
graph LR
    Faculty((Faculty Member))
    Admin((Administrator))
    StudentActor((Student))

    subgraph EduTrack CLI Boundary
        UC1[UC-1: Authenticate via Credentials]
        UC2[UC-2: View Enrolled Student Roster]
        UC3[UC-3: Search & Inspect Student Dossier]
        UC4[UC-4: Enter / Update Assessment Marks]
        UC5[UC-5: Review Cohort Analytics & Histograms]
        UC6[UC-6: Run Early Warning Risk Assessment]
        UC7[UC-7: Generate & Assign Interventions]
        UC8[UC-8: Resolve / Escalate Interventions]
        UC9[UC-9: Register New Student Accounts]
        UC10[UC-10: Export Cohort Reports to CSV]
        UC11[UC-11: View Personal Academic & Risk Status]
    end

    Faculty --> UC1
    Faculty --> UC2
    Faculty --> UC3
    Faculty --> UC4
    Faculty --> UC5
    Faculty --> UC6
    Faculty --> UC7
    Faculty --> UC8
    Faculty --> UC10

    Admin --> UC1
    Admin --> UC2
    Admin --> UC3
    Admin --> UC9
    Admin --> UC10

    StudentActor --> UC1
    StudentActor --> UC11
```

---

## 4. UML Class Diagram

Captures object-oriented class relationships, inheritance hierarchies, and design patterns:

```mermaid
classDiagram
    %% Inheritance Hierarchy
    class User {
        <<abstract>>
        -String username
        -String passwordHash
        -String fullName
        -String email
        -UserRole role
        +getDashboardCapabilities()* List~String~
    }

    class FacultyUser {
        -String department
        -String employeeId
        +getDashboardCapabilities() List~String~
    }

    class StudentUser {
        -String registrationNumber
        +getDashboardCapabilities() List~String~
    }

    class AdminUser {
        +getDashboardCapabilities() List~String~
    }

    User <|-- FacultyUser
    User <|-- StudentUser
    User <|-- AdminUser

    %% Domain Aggregates
    class Student {
        -String regNumber
        -String name
        -String email
        -String department
        -int semester
        -double cgpa
        -String mentorName
        -AcademicRecord academicRecord
        -RiskAssessment riskAssessment
        -List~Intervention~ interventions
        +addIntervention(Intervention)
    }

    class AcademicRecord {
        -String courseCode
        -double internalTest1
        -double internalTest2
        -double assignmentScore
        -double labScore
        -double quizScore
        -double attendancePercentage
        -double studyHoursPerWeek
        -int backlogsCount
        +calculateCompositeScore() double
        +isAttendanceDeficient() boolean
        +isFailingComposite() boolean
    }

    class RiskAssessment {
        -RiskLevel riskLevel
        -double riskScore
        -double failureProbability
        -double projectedFinalScore
        -List~String~ keyRiskFactors
        -String recommendation
    }

    class Intervention {
        -String id
        -String studentRegNumber
        -String studentName
        -String title
        -String strategyName
        -InterventionStatus status
        -String notes
    }

    Student *-- AcademicRecord
    Student *-- RiskAssessment
    Student o-- Intervention

    %% Strategy Pattern Hierarchy
    class InterventionStrategy {
        <<interface>>
        +getStrategyName() String
        +isApplicable(Student) boolean
        +createIntervention(Student, String) Intervention
    }

    class RemedialClassStrategy {
        +isApplicable(Student) boolean
        +createIntervention(Student, String) Intervention
    }

    class AttendanceCounselingStrategy {
        +isApplicable(Student) boolean
        +createIntervention(Student, String) Intervention
    }

    class PeerTutoringStrategy {
        +isApplicable(Student) boolean
        +createIntervention(Student, String) Intervention
    }

    InterventionStrategy <|.. RemedialClassStrategy
    InterventionStrategy <|.. AttendanceCounselingStrategy
    InterventionStrategy <|.. PeerTutoringStrategy

    %% Repository Hierarchy
    class StudentRepository {
        <<interface>>
        +save(Student) Student
        +findByRegNumber(String) Optional~Student~
        +findAll() List~Student~
        +deleteByRegNumber(String) boolean
    }

    class InMemoryStudentRepository {
        #Map~String, Student~ studentStore
    }

    class FileStudentRepository {
        -File storageFile
        -File seedFile
        +flush()
    }

    StudentRepository <|.. InMemoryStudentRepository
    InMemoryStudentRepository <|-- FileStudentRepository
```

---

## 5. UML Sequence Diagram: Assessment & Intervention Workflow

Illustrates method calls across layers during continuous assessment update and automated intervention generation:

```mermaid
sequenceDiagram
    autonumber
    actor Faculty as Faculty User
    participant CLI as CliController
    participant Svc as StudentService
    participant Repo as FileStudentRepository
    participant Engine as AnalyticsEngine
    participant IntSvc as InterventionService
    participant Strat as InterventionStrategy

    Faculty ->> CLI: Enter Marks (IT1, IT2, Lab, Quiz, Attendance)
    CLI ->> Svc: updateAcademicRecord(regNumber, newRecord)
    Svc ->> Repo: findByRegNumber(regNumber)
    Repo -->> Svc: Student object
    Svc ->> Svc: student.setAcademicRecord(newRecord)
    Svc ->> Repo: save(student)
    Repo ->> Repo: flush() to students_db.csv
    Repo -->> Svc: Saved Student

    CLI ->> Engine: assessStudentRisk(student)
    Engine ->> Engine: Calculate composite score & attendance deficit
    Engine ->> Engine: Compute failure probability & risk factors
    Engine -->> CLI: RiskAssessment (RiskLevel.HIGH)

    CLI ->> IntSvc: generateInterventionsForStudent(student)
    loop For each registered strategy
        IntSvc ->> Strat: isApplicable(student)
        Strat -->> IntSvc: true (deficiency detected)
        IntSvc ->> Strat: createIntervention(student, "INT-1005")
        Strat -->> IntSvc: Intervention instance
        IntSvc ->> Svc: student.addIntervention(intervention)
    end
    IntSvc -->> CLI: List of generated interventions
    CLI -->> Faculty: Render updated ASCII Dossier & Risk Gauge
```

---

## 6. Database / Storage Design

EduTrack utilizes a normalized, relational-equivalent CSV schema with in-memory hash indexing to ensure zero-dependency local execution while maintaining strict referential integrity.

### 6.1 Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    USER {
        string username PK
        string passwordHash
        string fullName
        string email
        string role
        datetime createdAt
    }

    STUDENT {
        string regNumber PK
        string name
        string email
        string department
        int semester
        float cgpa
        string mentorName
    }

    ACADEMIC_RECORD {
        string regNumber FK
        string courseCode
        float internalTest1
        float internalTest2
        float assignmentScore
        float labScore
        float quizScore
        float attendancePercentage
        float studyHoursPerWeek
        int backlogsCount
    }

    RISK_ASSESSMENT {
        string regNumber FK
        string riskLevel
        float riskScore
        float failureProbability
        float projectedFinalScore
        string keyRiskFactors
        datetime assessedAt
    }

    INTERVENTION {
        string id PK
        string studentRegNumber FK
        string studentName
        string title
        string strategyName
        string status
        string notes
        datetime createdAt
        datetime resolvedAt
    }

    USER ||--o{ STUDENT : advises
    STUDENT ||--|| ACADEMIC_RECORD : has
    STUDENT ||--|| RISK_ASSESSMENT : evaluated_with
    STUDENT ||--o{ INTERVENTION : receives
```

### 6.2 Data Schema Specification

| Entity | Field | Type | Constraint | Description |
| :--- | :--- | :--- | :--- | :--- |
| **Student** | `reg_number` | VARCHAR(20) | PRIMARY KEY, UNIQUE | Academic registration code (e.g. `23BCE1001`) |
| | `name` | VARCHAR(100) | NOT NULL | Full name of student |
| | `email` | VARCHAR(100) | NOT NULL, UNIQUE | University institutional email |
| | `department` | VARCHAR(50) | NOT NULL | Academic branch |
| | `semester` | INTEGER | CHECK (1..10) | Current enrolled semester |
| | `cgpa` | DOUBLE | CHECK (0.0..10.0) | Cumulative Grade Point Average |
| | `mentor_name` | VARCHAR(100) | NOT NULL | Faculty proctor name |
| **AcademicRecord** | `course_code` | VARCHAR(20) | NOT NULL | Course code (e.g. `CSE2001`) |
| | `internal_test_1` | DOUBLE | CHECK (0.0..50.0) | Internal Test 1 score |
| | `internal_test_2` | DOUBLE | CHECK (0.0..50.0) | Internal Test 2 score |
| | `assignment_score` | DOUBLE | CHECK (0.0..20.0) | Assignment submission mark |
| | `lab_score` | DOUBLE | CHECK (0.0..30.0) | Lab practical mark |
| | `quiz_score` | DOUBLE | CHECK (0.0..20.0) | Online quiz evaluation mark |
| | `attendance_percentage` | DOUBLE | CHECK (0.0..100.0) | Attendance rate (Statutory min: 75.0%) |
| | `study_hours_per_week` | DOUBLE | CHECK (>=0.0) | Weekly self-study allocation |
| | `backlogs_count` | INTEGER | CHECK (>=0) | Active standing arrears |
| **Intervention** | `id` | VARCHAR(20) | PRIMARY KEY | Unique ID (e.g. `INT-1001`) |
| | `student_reg_number` | VARCHAR(20) | FOREIGN KEY | Target student |
| | `strategy_name` | VARCHAR(60) | NOT NULL | Applied strategy name |
| | `status` | ENUM | NOT NULL | `PENDING`, `IN_PROGRESS`, `RESOLVED` |
| | `created_at` | DATETIME | NOT NULL | Creation timestamp |
