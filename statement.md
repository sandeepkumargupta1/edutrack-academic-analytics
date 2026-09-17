# Problem Statement & Project Scope: EduTrack

## 1. Problem Statement
In contemporary higher education institutions, student academic attrition and exam failures often occur because faculty identify learning deficiencies only after final semester examinations—when remediation is no longer viable. Continuous internal assessment marks, lab evaluations, weekly assignment submissions, and biometric attendance records are traditionally stored in disconnected spreadsheets or siloed portals. 

Without an automated, unified analytical mechanism:
1. **Late Identification**: Students falling below the statutory 75% attendance threshold or failing continuous assessment tests (CAT-1, CAT-2) are detected too late.
2. **Lack of Explainability**: Conventional grade books record numeric scores without pinpointing underlying causal drivers (e.g., compounding backlogs, low self-study allocation, or attendance degradation).
3. **No Systematic Intervention Tracking**: Remedial actions (such as specialized tutorials, peer mentor assignments, or counseling sessions) are coordinated informally without structured lifecycle monitoring.

**EduTrack** solves this problem by delivering a high-performance, modular Java Command-Line Interface (CLI) platform that continuously models academic risk, aggregates assessment metrics, computes probabilistic failure risk, and triggers automated, personalized academic intervention strategies before semester deadlines.

---

## 2. Scope of the Project
The scope of **EduTrack** encompasses:
- **Demographic & Course Data Management**: Maintaining student profile information (Registration Number, Name, Branch, Semester, CGPA, Faculty Advisor) and continuous course assessment data.
- **Continuous Assessment & Attendance Processing**: Recording and validating Internal Test 1, Internal Test 2, Lab Practicals, Quizzes, Assignments, and Attendance percentages.
- **Analytical Risk Evaluation Engine**: Running a multi-factor risk scoring algorithm that maps composite assessment metrics and attendance deficits to failure probabilities ($P(\text{Fail}) \in [0.0, 1.0]$) and classifies students into `LOW`, `MODERATE`, and `HIGH` risk tiers.
- **Automated Academic Interventions via Strategy Pattern**: Instantiating tailored remedial strategies (`RemedialClassStrategy`, `AttendanceCounselingStrategy`, `PeerTutoringStrategy`) based on specific diagnostic deficits.
- **Role-Based CLI & Reporting**: Providing role-based access for Faculty, Administrators, and Students, interactive ASCII data visualization (histograms, gauges, tables), and CSV export capabilities.
- **Automated Evaluator Headless Mode**: Enabling non-interactive batch evaluation (`--report`) for automated CI/CD and assessment grading pipelines.

---

## 3. Target Users
1. **Course Faculty & Instructors**:
   - Monitor cohort-wide performance, enter continuous assessment marks, identify struggling students, and supervise assigned remedial coursework.
2. **Academic Counselors & Faculty Mentors (Proctors)**:
   - Track attendance shortages, receive early warning risk notifications, and document counseling sessions and parental advisories.
3. **Department Heads & Academic Administrators**:
   - Review overall grade distribution histograms, audit cohort risk metrics, and assess the effectiveness of institutional remedial programs.
4. **Undergraduate Students**:
   - Review personal continuous assessment records, track attendance margins relative to the 75% mandatory threshold, and access assigned peer mentoring tasks.

---

## 4. High-Level Features
- **Role-Based Access Control (RBAC)**: Secure authentication with SHA-256 hashed credentials separating Faculty, Admin, and Student privileges.
- **Interactive ANSI-Bordered Console Interface**: Formatted tabular displays, colored risk badges, and clear navigation menus.
- **Statistical Analytics & ASCII Visualizations**: Real-time terminal bar charts and histograms illustrating grade distributions and risk breakdowns.
- **Early Warning Risk Engine**: Non-linear sigmoid risk mapping, identifying causal risk factors (attendance deficiency, backlog accumulation, internal test failure).
- **GoF Strategy Pattern Interventions**: Dynamic assignment and tracking of interventions with state transitions (`PENDING` $\to$ `IN_PROGRESS` $\to$ `RESOLVED`).
- **File Persistence & Bulk Ingestion**: Automated CSV loading, transactional in-memory indexing ($O(1)$ lookup), and export capabilities.
- **Dual Execution Modes**: Full interactive terminal session and zero-input headless evaluation mode (`--report`).
