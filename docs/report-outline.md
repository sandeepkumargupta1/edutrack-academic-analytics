# EduTrack Official Project Report Outline

> **Academic Report Structure Compliant with Section 6 of the VITyarthi Project Guidelines**  
> Grounded strictly in the actual Java SE CLI implementation.

---

## 1. Cover Page
- **Institution**: Vellore Institute of Technology (VIT), VITyarthi Flipped Learning Portal
- **Project Title**: EduTrack: Academic Performance Analytics & Student Intervention Platform
- **Course**: Object Oriented Programming using Java (CSE1007) / Software Engineering (CSE2001)
- **Student Name**: SANDEEP KUMAR GUPTA
- **Registration Number**: 25BAI10622
- **Branch / Program**: B.Tech Computer Science and Engineering in AIML
- **Faculty Guide**: [Faculty Guide Name]
- **Date**: Fall Semester 2026





---

## 2. Introduction
- Background on continuous assessment evaluation paradigms in collegiate engineering education.
- Examination of the limitations of reactive post-examination grading.
- Conceptual introduction of EduTrack as an automated, terminal-executable analytical platform.

---

## 3. Problem Statement
- Late identification of at-risk students and statutory attendance deficit violations ($<75.0\%$).
- Unidimensional gradebook metrics lacking causal risk driver diagnosis.
- Absence of auditable, systematic tracking of academic remedial interventions.

---

## 4. Functional Requirements
- **FR-01**: User Authentication via SHA-256 password hash verification.
- **FR-02**: Role-Based Access Control (`FacultyUser`, `AdminUser`, `StudentUser`).
- **FR-03**: Student Profile CRUD management with unique key constraints.
- **FR-04**: Continuous assessment mark entry and normalized composite scoring.
- **FR-05**: Attendance threshold monitoring ($75\%$) with penalty factor modeling.
- **FR-06**: Cohort analytics, averages, and grade distribution histograms.
- **FR-07**: Computational multi-factor risk analysis with sigmoid probability mapping.
- **FR-08**: Automated strategy-driven intervention generation (`RemedialClassStrategy`, `AttendanceCounselingStrategy`, `PeerTutoringStrategy`).
- **FR-09**: Intervention lifecycle state transitions (`PENDING` $\to$ `IN_PROGRESS` $\to$ `RESOLVED`).
- **FR-10**: File-backed CSV data persistence with transactional auto-syncing.
- **FR-11**: Headless non-interactive batch evaluation mode (`--report`).

---

## 5. Non-Functional Requirements
- **Performance**: $O(1)$ in-memory hash-indexed lookups; sub-15ms cohort risk evaluation across 1,000 records.
- **Security**: Cryptographic password hashing (SHA-256); method-level role capability enforcement.
- **Maintainability**: Strict 5-tier layered architecture separating CLI, Service, Strategy, Domain, and Repository layers.
- **Reliability**: Custom checked exception hierarchy preventing abrupt JVM crashes on malformed inputs.
- **Usability**: Bordered ASCII tables, ANSI color-coded status badges, and terminal histograms.

---

## 6. System Architecture
- Comprehensive 5-tier architecture:
  1. Presentation Layer (`CliController`, `TableRenderer`, `AsciiChartRenderer`)
  2. Service Layer (`AuthService`, `StudentService`, `AnalyticsEngine`, `InterventionService`)
  3. Strategy Layer (`InterventionStrategy`, `RemedialClassStrategy`, `AttendanceCounselingStrategy`, `PeerTutoringStrategy`)
  4. Domain Layer (`User`, `Student`, `AcademicRecord`, `RiskAssessment`, `Intervention`)
  5. Persistence Layer (`StudentRepository`, `InMemoryStudentRepository`, `FileStudentRepository`, `CsvHandler`)
- Reference: [`docs/architecture-diagram.png`](architecture-diagram.png)

---

## 7. Design Diagrams
- **Use Case Diagram**: Faculty, Admin, Student actor roles and 12 use cases ([`docs/use-case-diagram.png`](use-case-diagram.png)).
- **Workflow Diagram**: End-to-end operational execution flow ([`docs/workflow-diagram.png`](workflow-diagram.png)).
- **Sequence Diagram**: Mark entry, analytical risk scoring, and strategy dispatch ([`docs/sequence-diagram.png`](sequence-diagram.png)).
- **Class Diagram**: Real Java classes, inheritance, interfaces, and methods ([`docs/class-diagram.png`](class-diagram.png)).
- **ER / Storage Schema Diagram**: Flat-file CSV layout and entity relationships ([`docs/er-diagram.png`](er-diagram.png)).

---

## 8. Design Decisions & Rationale
- **Selection of CLI over GUI**: Adherence to VITyarthi automated evaluation guidelines requiring zero-GUI command-line executability.
- **Pure Java SE over Heavy Frameworks**: Elimination of classpath and configuration dependencies to ensure immediate execution on any machine.
- **GoF Strategy Pattern**: Ensuring open-closed extensibility for academic intervention policies.
- **In-Memory Caching with CSV Durability**: Combining $O(1)$ memory query speed with immediate file persistence.
- **Non-Linear Sigmoid Probability Curve**: Accurately reflecting compounding academic risk once attendance dips below statutory thresholds.

---

## 9. Implementation Details
- Code organization across 7 packages: `model`, `repository`, `service`, `strategy`, `exception`, `util`, `cli`.
- Java Collections Framework usage (`ConcurrentHashMap`, `ArrayList`, `TreeSet`).
- Stream API implementations for filtering, statistical aggregations, and sorting.
- Custom exception handling hierarchy.

---

## 10. Screenshots & Results
- Documented terminal sessions covering Login, Main Menu, Student Directory Table, Comprehensive Dossier, Cohort Histograms, Risk Alert Roster, and Intervention Plans.
- Reference: [`docs/screenshots/README.md`](screenshots/README.md).

---

## 11. Testing Approach
- Built-in reflection-based test harness (`com.edutrack.TestRunner`).
- 26 automated unit tests across 6 test modules covering authentication, student operations, risk calculations, strategy execution, input validation bounds, and CSV roundtrips.
- 100% test pass rate verified over 30 continuous automated runs.

---

## 12. Challenges Faced
- Managing cross-platform Windows/Linux terminal UTF-8 character encoding and ANSI escape sequences.
- Building a standalone zero-dependency unit testing harness without external JUnit JAR downloads.
- Formulating an explainable, sensitive multi-factor risk algorithm that reflects attendance penalties accurately.

---

## 13. Learnings & Key Takeaways
- Practical mastery of core OOP principles (Encapsulation, Polymorphism, Abstraction, Strategy Pattern, Repository Pattern).
- Designing resilient domain models with constructor-enforced invariant validations.
- Crafting ergonomic, visually appealing terminal interfaces using pure ASCII and ANSI formatting.

---

## 14. Future Enhancements
- Integration with institutional relational databases via optional JDBC/JPA repository drivers.
- Automated email notification dispatch to mentors and parents via JavaMail API.
- Multi-semester longitudinal tracking of student academic recovery.

---

## 15. References
- Gamma, E., et al. (1994). *Design Patterns: Elements of Reusable Object-Oriented Software*.
- Bloch, J. (2018). *Effective Java* (3rd ed.).
- Oracle Corporation. (2024). *Java SE Platform Documentation*.
- Vellore Institute of Technology. *Continuous Assessment Academic Regulations*.
