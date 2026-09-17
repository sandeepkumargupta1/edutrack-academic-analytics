package com.edutrack.service;

import com.edutrack.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Computational analytics and predictive risk engine for student cohorts.
 * Implements multi-factor statistical scoring, risk categorization, and cohort indicators.
 */
public class AnalyticsEngine {

    public static final double HIGH_RISK_THRESHOLD = 0.60;
    public static final double MODERATE_RISK_THRESHOLD = 0.35;

    public RiskAssessment assessStudentRisk(Student student) {
        AcademicRecord rec = student.getAcademicRecord();
        if (rec == null) {
            // Default assessment if no academic record yet
            RiskAssessment unassessed = new RiskAssessment(
                    RiskLevel.LOW, 10.0, 0.10, student.getCgpa() * 9.5,
                    Collections.singletonList("Awaiting first assessment cycle data."),
                    "No continuous assessment marks recorded yet."
            );
            student.setRiskAssessment(unassessed);
            return unassessed;
        }

        double composite = rec.calculateCompositeScore(); // Max 100
        double attendance = rec.getAttendancePercentage();
        int backlogs = rec.getBacklogsCount();
        double studyHours = rec.getStudyHoursPerWeek();
        double cgpa = student.getCgpa();

        // 1. Performance deficiency (0 - 50 pts)
        double scoreDeficiency = Math.max(0.0, (100.0 - composite) * 0.5);

        // 2. Attendance deficit (0 - 30 pts)
        double attendancePenalty = 0.0;
        if (attendance < AcademicRecord.ATTENDANCE_THRESHOLD) {
            attendancePenalty = (AcademicRecord.ATTENDANCE_THRESHOLD - attendance) * 1.2;
        }

        // 3. Backlogs penalty (0 - 25 pts)
        double backlogPenalty = Math.min(25.0, backlogs * 6.0);

        // 4. Study hours deficit (0 - 10 pts)
        double studyHoursPenalty = 0.0;
        if (studyHours < 7.0) {
            studyHoursPenalty = (7.0 - studyHours) * 1.5;
        }

        // Total calculated risk index out of 100
        double totalRiskIndex = Math.min(100.0, scoreDeficiency + attendancePenalty + backlogPenalty + studyHoursPenalty);

        // Sigmoid-style non-linear mapping to failure probability [0.0, 1.0]
        double failureProb = 1.0 / (1.0 + Math.exp(-0.08 * (totalRiskIndex - 45.0)));
        failureProb = Math.round(failureProb * 1000.0) / 1000.0;

        // Projected final exam score
        double projectedScore = composite * 0.7 + (cgpa / 10.0) * 30.0;
        if (attendance < AcademicRecord.ATTENDANCE_THRESHOLD) {
            projectedScore -= 8.0;
        }
        projectedScore = Math.max(10.0, Math.min(99.0, Math.round(projectedScore * 10.0) / 10.0));

        // Risk Level determination
        RiskLevel level;
        String recommendation;
        if (failureProb >= HIGH_RISK_THRESHOLD || (attendance < 60.0 && composite < 45.0)) {
            level = RiskLevel.HIGH;
            recommendation = "URGENT: Initiate faculty-led remedial coaching and schedule formal parental advisory meeting.";
        } else if (failureProb >= MODERATE_RISK_THRESHOLD || rec.isAttendanceDeficient() || backlogs > 0) {
            level = RiskLevel.MODERATE;
            recommendation = "ADVISORY: Assign peer mentor, conduct weekly tutorial checkpoints, and monitor attendance.";
        } else {
            level = RiskLevel.LOW;
            recommendation = "STABLE: Strong academic performance. Encourage participation in competitive coding/honors projects.";
        }

        // Attribute specific risk drivers
        List<String> factors = new ArrayList<>();
        if (rec.isAttendanceDeficient()) {
            factors.add(String.format("Attendance shortage: %.1f%% (< 75.0%% statutory threshold)", attendance));
        }
        if (rec.getInternalTest1() < 22.0) {
            factors.add(String.format("Internal Test 1 deficit: %.1f/50 marks", rec.getInternalTest1()));
        }
        if (rec.getInternalTest2() < 22.0) {
            factors.add(String.format("Internal Test 2 deficit: %.1f/50 marks", rec.getInternalTest2()));
        }
        if (backlogs > 0) {
            factors.add(String.format("%d active arrears/backlogs", backlogs));
        }
        if (studyHours < 6.0) {
            factors.add(String.format("Low self-study allocation: %.1f hrs/week (< 6.0 hrs/week)", studyHours));
        }
        if (cgpa < 6.5) {
            factors.add(String.format("Low cumulative GPA: %.2f (< 6.50)", cgpa));
        }
        if (factors.isEmpty()) {
            factors.add("Consistent attendance and assessment benchmarks achieved.");
        }

        RiskAssessment assessment = new RiskAssessment(level, totalRiskIndex, failureProb, projectedScore, factors, recommendation);
        student.setRiskAssessment(assessment);
        return assessment;
    }

    public void runCohortRiskAssessment(List<Student> students) {
        if (students != null) {
            for (Student s : students) {
                assessStudentRisk(s);
            }
        }
    }

    public double getAverageCgpa(List<Student> students) {
        if (students == null || students.isEmpty()) return 0.0;
        return students.stream().mapToDouble(Student::getCgpa).average().orElse(0.0);
    }

    public double getAverageAttendance(List<Student> students) {
        if (students == null || students.isEmpty()) return 0.0;
        return students.stream()
                .filter(s -> s.getAcademicRecord() != null)
                .mapToDouble(s -> s.getAcademicRecord().getAttendancePercentage())
                .average()
                .orElse(0.0);
    }

    public double getAverageCompositeScore(List<Student> students) {
        if (students == null || students.isEmpty()) return 0.0;
        return students.stream()
                .filter(s -> s.getAcademicRecord() != null)
                .mapToDouble(s -> s.getAcademicRecord().calculateCompositeScore())
                .average()
                .orElse(0.0);
    }

    public Map<RiskLevel, Integer> getRiskDistribution(List<Student> students) {
        Map<RiskLevel, Integer> distribution = new LinkedHashMap<>();
        distribution.put(RiskLevel.LOW, 0);
        distribution.put(RiskLevel.MODERATE, 0);
        distribution.put(RiskLevel.HIGH, 0);

        if (students != null) {
            for (Student s : students) {
                RiskAssessment ra = s.getRiskAssessment();
                if (ra == null) {
                    ra = assessStudentRisk(s);
                }
                distribution.put(ra.getRiskLevel(), distribution.get(ra.getRiskLevel()) + 1);
            }
        }
        return distribution;
    }

    public Map<String, Integer> getGradeDistribution(List<Student> students) {
        Map<String, Integer> grades = new LinkedHashMap<>();
        grades.put("S (>=90)", 0);
        grades.put("A (80-89)", 0);
        grades.put("B (70-79)", 0);
        grades.put("C (60-69)", 0);
        grades.put("D (50-59)", 0);
        grades.put("F (<50)", 0);

        if (students != null) {
            for (Student s : students) {
                if (s.getAcademicRecord() != null) {
                    double score = s.getAcademicRecord().calculateCompositeScore();
                    if (score >= 90.0) grades.put("S (>=90)", grades.get("S (>=90)") + 1);
                    else if (score >= 80.0) grades.put("A (80-89)", grades.get("A (80-89)") + 1);
                    else if (score >= 70.0) grades.put("B (70-79)", grades.get("B (70-79)") + 1);
                    else if (score >= 60.0) grades.put("C (60-69)", grades.get("C (60-69)") + 1);
                    else if (score >= 50.0) grades.put("D (50-59)", grades.get("D (50-59)") + 1);
                    else grades.put("F (<50)", grades.get("F (<50)") + 1);
                }
            }
        }
        return grades;
    }

    public List<Student> getAtRiskStudents(List<Student> students) {
        if (students == null) return Collections.emptyList();
        return students.stream()
                .filter(s -> {
                    RiskAssessment ra = s.getRiskAssessment();
                    if (ra == null) ra = assessStudentRisk(s);
                    return ra.getRiskLevel() == RiskLevel.HIGH || ra.getRiskLevel() == RiskLevel.MODERATE;
                })
                .sorted((s1, s2) -> Double.compare(
                        s2.getRiskAssessment().getFailureProbability(),
                        s1.getRiskAssessment().getFailureProbability()
                ))
                .collect(Collectors.toList());
    }
}
