# EduTrack Terminal Screenshots & Verification Guide

This directory documents the representative terminal outputs of the **EduTrack Academic Analytics CLI Platform**. 

Because EduTrack is engineered as a **100% pure terminal Command-Line Interface (CLI)** application to meet the strict terminal executability criteria of the VITyarthi evaluation guidelines, visual evaluation is based on terminal screens. Evaluators and students can capture visual screenshots of their terminal window corresponding to each of the eight primary functional screens documented below.

---

## Screenshot 1: System Authentication & Role Login Screen

### Capture Command:
```cmd
run.bat
```
*(Enter username `faculty` and password `admin123`)*

### Representative Terminal Output:
```
================================================================================
   EduTrack - Academic Performance Analytics & Student Intervention Platform
   VITyarthi Flipped Course Evaluation Edition | Pure Java CLI System
================================================================================

>>> SYSTEM LOGIN
Default Test Credentials: [faculty / admin123], [admin / root123], [student / student123]
Username: faculty
Password: ********

[SUCCESS] Welcome, Dr. K. Raman (Faculty Member)!
```

---

## Screenshot 2: Interactive Main Navigation Menu

### Representation:
The main menu dynamically renders authorized operational choices according to the logged-in user's role.

### Representative Terminal Output:
```
=== MAIN MENU | Logged in as: faculty (FACULTY) ===
 [ 1] View Enrolled Student Directory
 [ 2] Search Student by Reg No / Name / Department
 [ 3] Detailed Student Profile & Risk Breakdown
 [ 4] Register New Student Profile
 [ 5] Update Continuous Assessment Marks & Attendance
 [ 6] Cohort Analytics & ASCII Visual Charts
 [ 7] Run Early Risk Detection & Alert Roster
 [ 8] Generate Automated Academic Interventions
 [ 9] View & Manage Intervention Action Plans
 [10] Export Performance & Risk Report to CSV
 [11] Logout / Switch User
 [ 0] Exit Application

Enter choice: 
```

---

## Screenshot 3: Student Management & Directory Table

### Capture Command:
Select option `1` from the main menu.

### Representative Terminal Output:
```
--- ENROLLED STUDENT DIRECTORY (20 Students) ---
+-----------+---------------------+--------------------------------+-----+------+------------+---------------+------------+
|  Reg No   |        Name         |           Department           | Sem | CGPA | Attendance | Composite/100 | Risk Level |
+-----------+---------------------+--------------------------------+-----+------+------------+---------------+------------+
| 23BCE1001 | Aarav Sharma        | Computer Science & Engineering | 5   | 8.85 | 94.5%      | 92.0          | LOW        |
| 23BCE1002 | Diya Patel          | Computer Science & Engineering | 5   | 9.12 | 96.0%      | 95.6          | LOW        |
| 23BCE1003 | Rohan Verma         | Computer Science & Engineering | 5   | 6.40 | 62.0%      | 47.0          | HIGH       |
| 23BCE1004 | Ananya Iyer         | Information Technology         | 5   | 8.20 | 88.0%      | 79.0          | LOW        |
| 23BCE1005 | Vikram Malhotra     | Computer Science & Engineering | 5   | 5.75 | 54.0%      | 36.3          | HIGH       |
| 23BCE1006 | Sneha Reddy         | Computer Science & Engineering | 5   | 7.95 | 82.5%      | 74.3          | LOW        |
| 23BCE1007 | Kabir Mehta         | Information Technology         | 5   | 6.80 | 72.0%      | 57.8          | MODERATE   |
| 23BCE1008 | Pooja Nair          | Computer Science & Engineering | 5   | 8.60 | 91.0%      | 86.6          | LOW        |
| 23BCE1009 | Aditya Deshmukh     | Computer Science & Engineering | 5   | 5.20 | 48.5%      | 31.4          | HIGH       |
| 23BCE1010 | Meera Joshi         | Information Technology         | 5   | 7.45 | 79.0%      | 67.0          | LOW        |
+-----------+---------------------+--------------------------------+-----+------+------------+---------------+------------+
```

---

## Screenshot 4: Comprehensive Student Academic Dossier

### Capture Command:
Select option `3` and enter registration number `23BCE1005`.

### Representative Terminal Output:
```
================================================================================
   STUDENT ACADEMIC DOSSIER: Vikram Malhotra (23BCE1005)
================================================================================
 Department: Computer Science & Engineering | Semester: 5 | CGPA: 5.75
 Faculty Mentor: Dr. M. Sundar | Email: vikram.malhotra2023@vitstudent.ac.in

--- Continuous Assessment Record (Course: CSE2001) ---
 Internal Test 1: 16.5 / 50.0   | Internal Test 2: 14.0 / 50.0
 Assignment:      9.0 / 20.0    | Lab Practical:   13.0 / 30.0
 Quiz Score:      7.5 / 20.0    | Study Hours/Wk:  4.0 hrs
 Attendance:      54.0%         | Active Backlogs: 3
 Normalized Continuous Score: 36.3 / 100.0

--- Predictive Risk Analytics & Forecasting ---
 Risk Classification: HIGH RISK
 Failure Probability: [███████████████████░] 94.1%
 Projected Final Score: 34.2 / 100.0
 Key Risk Drivers:
   • Attendance shortage: 54.0% (< 75.0% statutory threshold)
   • Internal Test 1 deficit: 16.5/50 marks
   • Internal Test 2 deficit: 14.0/50 marks
   • 3 active arrears/backlogs
   • Low self-study allocation: 4.0 hrs/week (< 6.0 hrs/week)
   • Low cumulative GPA: 5.75 (< 6.50)
 Recommended Action: URGENT: Initiate faculty-led remedial coaching and schedule formal parental advisory meeting.

--- Active & Assigned Interventions ---
 • [INT-1002] Mandatory Remedial Classes in CSE2001 (Status: Action Pending) - Assigned 8 hours of targeted subject tutorials.
 • [INT-1003] Urgent Attendance Advisory & Parental Notification (Status: Action Pending) - Attendance currently at 54.0%.
================================================================================
```

---

## Screenshot 5: Cohort Analytics & ASCII Visual Histograms

### Capture Command:
Select option `6` from the main menu.

### Representative Terminal Output:
```
================================================================================
                   COHORT ANALYTICS & STATISTICAL INDICATORS
================================================================================
 Total Enrolled Students: 20
 Cohort Average CGPA:     7.49 / 10.0
 Cohort Average Att.:     78.0%
 Cohort Composite Score:  67.9 / 100.0

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
================================================================================
```

---

## Screenshot 6: Early-Warning Risk Detection Alert Roster

### Capture Command:
Select option `7` from the main menu.

### Representative Terminal Output:
```
--- EARLY RISK DETECTION & EARLY-WARNING ALERT ROSTER (8 Flagged) ---
+-----------+------------------+-------+----------+------------+------------+----------------------------------------------------------+
|  Reg No   |   Student Name   | Att.  | Comp/100 | Risk Level | Fail Prob. |                   Primary Risk Drivers                   |
+-----------+------------------+-------+----------+------------+------------+----------------------------------------------------------+
| 23BCE1009 | Aditya Deshmukh  | 48.5% | 31.4     | HIGH       | 98.2%      | Attendance shortage: 48.5% (< 75.0% statutory threshold) |
| 23BCE1005 | Vikram Malhotra  | 54.0% | 36.3     | HIGH       | 94.1%      | Attendance shortage: 54.0% (< 75.0% statutory threshold) |
| 23BCE1015 | Devendra Yadav   | 58.0% | 39.5     | HIGH       | 83.9%      | Attendance shortage: 58.0% (< 75.0% statutory threshold) |
| 23BCE1003 | Rohan Verma      | 62.0% | 47.0     | HIGH       | 71.3%      | Attendance shortage: 62.0% (< 75.0% statutory threshold) |
| 23BCE1012 | Riya Sen         | 66.0% | 48.0     | HIGH       | 60.5%      | Attendance shortage: 66.0% (< 75.0% statutory threshold) |
| 23BCE1019 | Nikhil Choudhary | 68.5% | 53.1     | MODERATE   | 35.0%      | Attendance shortage: 68.5% (< 75.0% statutory threshold) |
| 23BCE1007 | Kabir Mehta      | 72.0% | 57.8     | MODERATE   | 24.2%      | Attendance shortage: 72.0% (< 75.0% statutory threshold) |
| 23BCE1017 | Varun Nambiar    | 74.5% | 61.2     | MODERATE   | 17.9%      | Attendance shortage: 74.5% (< 75.0% statutory threshold) |
+-----------+------------------+-------+----------+------------+------------+----------------------------------------------------------+
```

---

## Screenshot 7: Strategy-Driven Intervention Management

### Capture Command:
Select option `9` from the main menu.

### Representative Terminal Output:
```
--- ACTIVE & ASSIGNED INTERVENTION PLANS (21 Total) ---
+----------+-----------+------------------+--------------------------------------------------+------------------------------+-------------+------------------+
|    ID    |  Reg No   |   Student Name   |                Intervention Title                |           Strategy           |   Status    | Assigned Mentor  |
+----------+-----------+------------------+--------------------------------------------------+------------------------------+-------------+------------------+
| INT-1001 | 23BCE1003 | Rohan Verma      | Mandatory Remedial Classes in CSE2001            | Remedial Coaching & Doubt    | PENDING     | Dr. M. Sundar    |
| INT-1002 | 23BCE1003 | Rohan Verma      | Urgent Attendance Advisory & Parental Notice     | Attendance Shortage Advisory | IN PROGRESS | Dr. M. Sundar    |
| INT-1003 | 23BCE1003 | Rohan Verma      | Peer Mentorship & Collaborative Learning Circle  | Peer Tutoring Pairing        | RESOLVED    | Dr. M. Sundar    |
| INT-1004 | 23BCE1005 | Vikram Malhotra  | Mandatory Remedial Classes in CSE2001            | Remedial Coaching & Doubt    | PENDING     | Dr. M. Sundar    |
| INT-1005 | 23BCE1005 | Vikram Malhotra  | Urgent Attendance Advisory & Parental Notice     | Attendance Shortage Advisory | PENDING     | Dr. M. Sundar    |
+----------+-----------+------------------+--------------------------------------------------+------------------------------+-------------+------------------+
```

---

## Screenshot 8: Automated Unit Test Suite Execution

### Capture Command:
```cmd
test.bat
```

### Representative Terminal Output:
```
Executing Automated Test Suite...

================================================================================
                   EDUTRACK AUTOMATED UNIT TEST RUNNER
================================================================================

Running: AuthServiceTest
  ✔ testPasswordHashingConsistency                [PASSED]
  ✔ testInvalidPasswordRejection                  [PASSED]
  ✔ testValidFacultyLogin                         [PASSED]
  ✔ testUnknownUserRejection                      [PASSED]
  ✔ testRoleAuthorization                         [PASSED]

Running: StudentServiceTest
  ✔ testDuplicateRegistrationRejection            [PASSED]
  ✔ testCreateAndRetrieveStudent                  [PASSED]
  ✔ testNonExistentStudentThrowsException         [PASSED]
  ✔ testSearchStudents                            [PASSED]
  ✔ testDeleteStudent                             [PASSED]
  ✔ testUpdateAcademicRecord                      [PASSED]

Running: AnalyticsEngineTest
  ✔ testHighRiskStudentAssessment                 [PASSED]
  ✔ testCohortAveragesCalculation                 [PASSED]
  ✔ testAttendanceDeficiencyDetection             [PASSED]
  ✔ testLowRiskStudentAssessment                  [PASSED]
  ✔ testGradeDistributionBuckets                  [PASSED]

Running: InterventionServiceTest
  ✔ testUpdateInterventionStatus                  [PASSED]
  ✔ testRemedialInterventionGeneratedForLowInternals [PASSED]
  ✔ testAttendanceInterventionGeneratedForDeficientAttendance [PASSED]

Running: InputValidationAndSecurityTest
  ✔ testNegativeInternalMarksRejected             [PASSED]
  ✔ testExcessiveInternalMarksRejected            [PASSED]
  ✔ testAttendanceOutOfBoundsRejected             [PASSED]
  ✔ testNegativeStudyHoursAndBacklogsRejected     [PASSED]
  ✔ testRoleCapabilitySegregation                 [PASSED]
  ✔ testInputValidatorRegNoAndEmail               [PASSED]

Running: CsvHandlerTest
  ✔ testSaveAndLoadRoundtrip                      [PASSED]

================================================================================
 TOTAL TESTS: 26 | PASSED: 26 | FAILED: 0
================================================================================
ALL TESTS PASSED SUCCESSFULLY! (100% Pass Rate across 26 unit tests)
```
