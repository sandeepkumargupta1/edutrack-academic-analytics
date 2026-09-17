# Problem Statement & Project Scope: EduTrack

> **VITyarthi Flipped Course Evaluated Project Submission**  
> **Course**: Object Oriented Programming using Java (CSE1007) / Software Engineering (CSE2001)  
> **Platform**: 100% Pure Java SE Command-Line Interface (CLI) System  
> **Repository**: `https://github.com/sandeepkumargupta1/edutrack-academic-analytics`

---

## 1. Problem Statement

In contemporary higher education institutions, student academic attrition and final examination failures frequently occur because faculty mentors identify learning deficiencies only after end-semester examinations are completed—when remediation is no longer viable. Continuous internal assessment test scores (CAT-1, CAT-2), weekly laboratory evaluations, assignment submissions, and biometric attendance records are traditionally recorded in isolated spreadsheets or decentralized university portals.

Without an automated, unified analytical mechanism:
1. **Delayed Identification**: Students falling below the statutory 75.0% attendance threshold or failing continuous assessment tests are detected too late.
2. **Lack of Causal Attribution**: Conventional grade books record raw scores without diagnosing underlying causal factors (e.g., compounding backlogs, low self-study allocation, or attendance degradation).
3. **No Systematic Intervention Tracking**: Remedial actions (such as specialized tutorials, peer mentor assignments, or counseling sessions) are coordinated informally without structured lifecycle monitoring.

**EduTrack** solves this problem by delivering a high-performance, modular Java Command-Line Interface (CLI) platform that continuously models academic risk, aggregates assessment metrics, computes probabilistic failure risk, and triggers automated, personalized academic intervention strategies before semester deadlines.

---

## 2. Project Scope

### In Scope:
- **Demographic & Profile Management**: Enrolling students, assigning registration numbers, departments, semesters, CGPA, and faculty advisors.
- **Continuous Assessment Tracking**: Ingesting and validating Internal Test 1, Internal Test 2, Lab Practicals, Quizzes, Assignments, and Attendance percentages.
- **Analytical Risk Evaluation**: Running a multi-factor risk scoring algorithm that maps composite assessment metrics and attendance deficits to failure probabilities ($P(\text{Fail}) \in [0.0, 1.0]$) and classifies students into `LOW`, `MODERATE`, and `HIGH` risk tiers.
- **Automated Academic Interventions via Strategy Pattern**: Instantiating tailored remedial strategies (`RemedialClassStrategy`, `AttendanceCounselingStrategy`, `PeerTutoringStrategy`) based on specific diagnostic deficits.
- **Role-Based CLI & Reporting**: Providing role-based access for Faculty, Administrators, and Students, interactive ASCII data visualization (histograms, gauges, tables), and CSV export capabilities.
- **Automated Evaluator Headless Mode (`--report`)**: Enabling non-interactive batch evaluation for automated CI/CD and assessment grading pipelines.

### Out of Scope:
- Graphical desktop windowing systems (Swing/JavaFX) and web browsers, complying with the strict terminal execution mandate of the evaluation guidelines.
- Heavyweight external relational database server daemons (MySQL/PostgreSQL), preserving zero-dependency local portability.

---

## 3. Target Users

1. **Course Faculty & Instructors**:
   - Monitor cohort-wide performance, enter continuous assessment marks, identify struggling students, and supervise assigned remedial coursework.
2. **Academic Counselors & Faculty Mentors (Proctors)**:
   - Track attendance shortages, receive early warning risk notifications, and document counseling sessions and parental advisories.
3. **Department Heads & Academic Administrators**:
   - Review overall grade distribution histograms, audit cohort risk metrics, and assess the effectiveness of institutional remedial programs.
4. **Enrolled Undergraduate Students**:
   - Review personal continuous assessment records, track attendance margins relative to the 75% mandatory threshold, and access assigned peer mentoring tasks.

---

## 4. Objectives

- Design and implement a robust, object-oriented Java SE application adhering to clean architecture.
- Formulate a transparent multi-factor analytical scoring algorithm to compute failure probability without black-box ML dependencies.
- Automate the assignment of personalized remedial interventions using the GoF Strategy Pattern.
- Deliver rich terminal visualizations (ASCII histograms and risk gauges) for enhanced evaluator usability.
- Provide comprehensive automated unit testing verifying models, business logic, and file storage with 100% pass rates.
- Maintain durable CSV file storage with transactional in-memory indexing ($O(1)$ lookup).

---

## 5. High-Level Features

- **Role-Based Access Control (RBAC)**: Secure authentication with SHA-256 hashed credentials separating Faculty, Admin, and Student privileges.
- **Interactive ANSI-Bordered Console Interface**: Formatted tabular displays, colored risk badges, and clear navigation menus.
- **Statistical Analytics & ASCII Visualizations**: Real-time terminal bar charts and histograms illustrating grade distributions and risk breakdowns.
- **Early Warning Risk Engine**: Non-linear sigmoid risk mapping, identifying causal risk factors (attendance deficiency, backlog accumulation, internal test failure).
- **GoF Strategy Pattern Interventions**: Dynamic assignment and tracking of interventions with state transitions (`PENDING` $\to$ `IN_PROGRESS` $\to$ `RESOLVED`).
- **File Persistence & Bulk Ingestion**: Automated CSV loading, transactional in-memory indexing ($O(1)$ lookup), and export capabilities.
- **Dual Execution Modes**: Full interactive terminal session and zero-input headless evaluation mode (`--report`).

---

## 6. Major Functional Modules

1. **Module 1: Authentication & Authorization (RBAC)**
   - SHA-256 password hashing.
   - User hierarchy: `User` base class, `FacultyUser`, `AdminUser`, `StudentUser`.
2. **Module 2: Student Management & Academic Record Tracking**
   - Profile management with primary key uniqueness.
   - Continuous assessment entry with range validation (IT1, IT2, Lab, Quiz, Attendance).
3. **Module 3: Computational Risk Assessment Engine**
   - Normalized continuous score calculation out of 100.
   - Statutory attendance penalty modeling ($<75\%$).
   - Sigmoid probabilistic risk scoring.
4. **Module 4: Strategy-Driven Intervention Engine**
   - GoF Strategy Pattern for tailored remedial generation.
   - Action item lifecycle management.
5. **Module 5: Terminal Presentation & Headless Audit**
   - Bordered ASCII table rendering.
   - ASCII grade distribution histograms and risk breakdown bar charts.
   - Non-interactive batch execution (`--report`).

---

## 7. Expected Input

| Input Category | Specific Fields | Valid Range / Format | Validation Rule |
| :--- | :--- | :--- | :--- |
| **Student Demographics** | Registration Number | String (e.g. `23BCE1001`) | Regex `^[0-9]{2}[A-Za-z]{3}[0-9]{4}$`, Unique |
| | Full Name | String (e.g. `Aarav Sharma`) | Non-empty, 2–100 characters |
| | Email Address | String (e.g. `aarav@vitstudent.ac.in`) | Standard RFC email regex, Unique |
| | Academic Department | String | Non-empty text |
| | Enrolled Semester | Integer | Integer between 1 and 10 |
| | Cumulative GPA (CGPA) | Floating-point | Decimal between 0.0 and 10.0 |
| **Continuous Assessments** | Internal Test 1 (IT1) | Floating-point | Decimal between 0.0 and 50.0 |
| | Internal Test 2 (IT2) | Floating-point | Decimal between 0.0 and 50.0 |
| | Assignment Mark | Floating-point | Decimal between 0.0 and 20.0 |
| | Lab Practical Mark | Floating-point | Decimal between 0.0 and 30.0 |
| | Quiz Mark | Floating-point | Decimal between 0.0 and 20.0 |
| | Attendance Percentage | Floating-point | Decimal between 0.0% and 100.0% |
| | Self-Study Hours/Week | Floating-point | Decimal $\ge 0.0$ hours |
| | Active Backlogs Count | Integer | Integer $\ge 0$ |

---

## 8. Expected Output

| Output Category | Generated Information | Format / Presentation |
| :--- | :--- | :--- |
| **Student Performance Dossier** | Comprehensive profile, continuous marks breakdown, normalized composite score out of 100 | Bordered ASCII table with ANSI color highlights |
| **Risk Assessment Results** | Risk Level (`LOW`, `MODERATE`, `HIGH`), Probabilistic Failure Risk ($P(\text{Fail}) \in [0.0, 1.0]$), Projected Final Exam Mark | ANSI risk gauge (`[██████████░░░░░░░░░░] 48.0%`) and explicit causal risk factors list |
| **Actionable Interventions** | Generated action title, strategy applied, proctor assignment, meeting notes | Bordered intervention roster with status badge (`PENDING`, `IN_PROGRESS`, `RESOLVED`) |
| **Cohort Analytics** | Cohort Average CGPA, Cohort Average Attendance, Cohort Average Composite Score | Summary KPI cards and ASCII horizontal bar chart distribution histograms |
| **CSV Export** | Full snapshot of student profiles, assessments, and metrics | UTF-8 formatted comma-separated flat file (`data/export/edutrack_export_*.csv`) |

---

## 9. Limitations

1. **Single-Node Execution**: EduTrack is designed as a standalone terminal system; it does not currently support distributed multi-node clustering.
2. **File-Based Storage Concurrency**: Persistence relies on a synchronized flat-file CSV backing an in-memory `ConcurrentHashMap`. While optimal for single-institution course cohorts (up to several thousand records), it does not provide distributed database ACID transactions across concurrent network nodes.
3. **Command-Line Interface Focus**: EduTrack deliberately avoids graphical windowing toolkits (Swing/JavaFX) and web servers in order to maintain 100% zero-dependency terminal executability for automated academic grading pipelines.
