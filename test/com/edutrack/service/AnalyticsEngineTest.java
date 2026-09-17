package com.edutrack.service;

import com.edutrack.TestRunner.Assertion;
import com.edutrack.model.AcademicRecord;
import com.edutrack.model.RiskAssessment;
import com.edutrack.model.RiskLevel;
import com.edutrack.model.Student;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AnalyticsEngineTest {

    public void testLowRiskStudentAssessment() {
        AnalyticsEngine engine = new AnalyticsEngine();
        Student s = new Student("23BCE1001", "Top Student", "top@vit.ac.in", "CSE", 5, 9.2, "Dr. Guide");
        AcademicRecord rec = new AcademicRecord("CSE2001", 48.0, 47.0, 19.0, 28.0, 19.0, 95.0, 20.0, 0);
        s.setAcademicRecord(rec);

        RiskAssessment ra = engine.assessStudentRisk(s);
        Assertion.assertEquals(RiskLevel.LOW, ra.getRiskLevel(), "High performing student should be classified LOW risk");
        Assertion.assertTrue(ra.getFailureProbability() < 0.35, "Failure probability should be low");
    }

    public void testHighRiskStudentAssessment() {
        AnalyticsEngine engine = new AnalyticsEngine();
        Student s = new Student("23BCE1005", "Struggling Student", "strug@vit.ac.in", "CSE", 5, 5.2, "Dr. Guide");
        // Low attendance (50%), low internals (15/50), backlogs (3)
        AcademicRecord rec = new AcademicRecord("CSE2001", 15.0, 14.0, 8.0, 12.0, 6.0, 50.0, 3.0, 3);
        s.setAcademicRecord(rec);

        RiskAssessment ra = engine.assessStudentRisk(s);
        Assertion.assertEquals(RiskLevel.HIGH, ra.getRiskLevel(), "Struggling student must be flagged HIGH risk");
        Assertion.assertTrue(ra.getFailureProbability() >= 0.60, "Failure probability should exceed high risk threshold");
        Assertion.assertTrue(ra.getKeyRiskFactors().size() >= 2, "Should identify multiple causal risk factors");
    }

    public void testAttendanceDeficiencyDetection() {
        AcademicRecord recLow = new AcademicRecord("CSE2001", 30.0, 30.0, 15.0, 20.0, 15.0, 68.0, 10.0, 0);
        AcademicRecord recGood = new AcademicRecord("CSE2001", 30.0, 30.0, 15.0, 20.0, 15.0, 85.0, 10.0, 0);

        Assertion.assertTrue(recLow.isAttendanceDeficient(), "Attendance 68% must be deficient (< 75%)");
        Assertion.assertFalse(recGood.isAttendanceDeficient(), "Attendance 85% must not be deficient");
    }

    public void testCohortAveragesCalculation() {
        AnalyticsEngine engine = new AnalyticsEngine();
        Student s1 = new Student("23BCE1001", "S1", "s1@vit.ac.in", "CSE", 5, 8.0, "Dr. Guide");
        s1.setAcademicRecord(new AcademicRecord("CSE2001", 40.0, 40.0, 16.0, 24.0, 16.0, 90.0, 15.0, 0));

        Student s2 = new Student("23BCE1002", "S2", "s2@vit.ac.in", "CSE", 5, 6.0, "Dr. Guide");
        s2.setAcademicRecord(new AcademicRecord("CSE2001", 20.0, 20.0, 10.0, 15.0, 10.0, 70.0, 5.0, 1));

        List<Student> list = Arrays.asList(s1, s2);
        Assertion.assertEquals(7.0, engine.getAverageCgpa(list), 0.001, "Average CGPA should be 7.0");
        Assertion.assertEquals(80.0, engine.getAverageAttendance(list), 0.001, "Average attendance should be 80.0%");
    }

    public void testGradeDistributionBuckets() {
        AnalyticsEngine engine = new AnalyticsEngine();
        Student s1 = new Student("23BCE1001", "S1", "s1@vit.ac.in", "CSE", 5, 9.0, "Dr. Guide");
        // Score = 25 + 25 + 15 + 20 + 15 = 100.0 (S Grade)
        s1.setAcademicRecord(new AcademicRecord("CSE2001", 50.0, 50.0, 20.0, 30.0, 20.0, 95.0, 15.0, 0));

        Map<String, Integer> grades = engine.getGradeDistribution(Arrays.asList(s1));
        Assertion.assertEquals(1, grades.get("S (>=90)"), "1 student in S grade bucket");
        Assertion.assertEquals(0, grades.get("F (<50)"), "0 students in F grade bucket");
    }
}
