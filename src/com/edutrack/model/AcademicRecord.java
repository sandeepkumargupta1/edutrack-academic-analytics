package com.edutrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain entity capturing continuous assessment and attendance tracking metrics.
 * Demonstrates encapsulation with invariant verification.
 */
public class AcademicRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final double ATTENDANCE_THRESHOLD = 75.0;
    public static final double PASSING_COMPOSITE_THRESHOLD = 50.0;

    private String courseCode;
    private double internalTest1;      // Max 50
    private double internalTest2;      // Max 50
    private double assignmentScore;    // Max 20
    private double labScore;           // Max 30
    private double quizScore;          // Max 20
    private double attendancePercentage; // 0.0 - 100.0
    private double studyHoursPerWeek;
    private int backlogsCount;
    private LocalDateTime lastUpdated;

    public AcademicRecord(String courseCode, double internalTest1, double internalTest2,
                          double assignmentScore, double labScore, double quizScore,
                          double attendancePercentage, double studyHoursPerWeek, int backlogsCount) {
        validateInputs(internalTest1, internalTest2, assignmentScore, labScore, quizScore, attendancePercentage, studyHoursPerWeek, backlogsCount);
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "CSE2001";
        this.internalTest1 = internalTest1;
        this.internalTest2 = internalTest2;
        this.assignmentScore = assignmentScore;
        this.labScore = labScore;
        this.quizScore = quizScore;
        this.attendancePercentage = attendancePercentage;
        this.studyHoursPerWeek = studyHoursPerWeek;
        this.backlogsCount = backlogsCount;
        this.lastUpdated = LocalDateTime.now();
    }

    private void validateInputs(double it1, double it2, double asgn, double lab, double quiz,
                                double att, double hours, int backlogs) {
        if (it1 < 0.0 || it1 > 50.0) throw new IllegalArgumentException("Internal Test 1 must be between 0 and 50");
        if (it2 < 0.0 || it2 > 50.0) throw new IllegalArgumentException("Internal Test 2 must be between 0 and 50");
        if (asgn < 0.0 || asgn > 20.0) throw new IllegalArgumentException("Assignment score must be between 0 and 20");
        if (lab < 0.0 || lab > 30.0) throw new IllegalArgumentException("Lab score must be between 0 and 30");
        if (quiz < 0.0 || quiz > 20.0) throw new IllegalArgumentException("Quiz score must be between 0 and 20");
        if (att < 0.0 || att > 100.0) throw new IllegalArgumentException("Attendance percentage must be between 0 and 100");
        if (hours < 0.0) throw new IllegalArgumentException("Study hours cannot be negative");
        if (backlogs < 0) throw new IllegalArgumentException("Backlogs count cannot be negative");
    }

    /**
     * Computes the normalized continuous assessment composite mark out of 100.
     * IT1 (50->25) + IT2 (50->25) + Assignment (20->15) + Lab (30->20) + Quiz (20->15) = 100
     */
    public double calculateCompositeScore() {
        double it1Scaled = (internalTest1 / 50.0) * 25.0;
        double it2Scaled = (internalTest2 / 50.0) * 25.0;
        double asgnScaled = (assignmentScore / 20.0) * 15.0;
        double labScaled = (labScore / 30.0) * 20.0;
        double quizScaled = (quizScore / 20.0) * 15.0;
        return it1Scaled + it2Scaled + asgnScaled + labScaled + quizScaled;
    }

    public boolean isAttendanceDeficient() {
        return this.attendancePercentage < ATTENDANCE_THRESHOLD;
    }

    public boolean isFailingComposite() {
        return calculateCompositeScore() < PASSING_COMPOSITE_THRESHOLD;
    }

    // Getters and Setters
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "CSE2001";
        this.lastUpdated = LocalDateTime.now();
    }

    public double getInternalTest1() {
        return internalTest1;
    }

    public void setInternalTest1(double internalTest1) {
        if (internalTest1 < 0.0 || internalTest1 > 50.0) {
            throw new IllegalArgumentException("Internal Test 1 must be between 0 and 50");
        }
        this.internalTest1 = internalTest1;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getInternalTest2() {
        return internalTest2;
    }

    public void setInternalTest2(double internalTest2) {
        if (internalTest2 < 0.0 || internalTest2 > 50.0) {
            throw new IllegalArgumentException("Internal Test 2 must be between 0 and 50");
        }
        this.internalTest2 = internalTest2;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getAssignmentScore() {
        return assignmentScore;
    }

    public void setAssignmentScore(double assignmentScore) {
        if (assignmentScore < 0.0 || assignmentScore > 20.0) {
            throw new IllegalArgumentException("Assignment score must be between 0 and 20");
        }
        this.assignmentScore = assignmentScore;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getLabScore() {
        return labScore;
    }

    public void setLabScore(double labScore) {
        if (labScore < 0.0 || labScore > 30.0) {
            throw new IllegalArgumentException("Lab score must be between 0 and 30");
        }
        this.labScore = labScore;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getQuizScore() {
        return quizScore;
    }

    public void setQuizScore(double quizScore) {
        if (quizScore < 0.0 || quizScore > 20.0) {
            throw new IllegalArgumentException("Quiz score must be between 0 and 20");
        }
        this.quizScore = quizScore;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        if (attendancePercentage < 0.0 || attendancePercentage > 100.0) {
            throw new IllegalArgumentException("Attendance percentage must be between 0 and 100");
        }
        this.attendancePercentage = attendancePercentage;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getStudyHoursPerWeek() {
        return studyHoursPerWeek;
    }

    public void setStudyHoursPerWeek(double studyHoursPerWeek) {
        if (studyHoursPerWeek < 0.0) throw new IllegalArgumentException("Study hours cannot be negative");
        this.studyHoursPerWeek = studyHoursPerWeek;
        this.lastUpdated = LocalDateTime.now();
    }

    public int getBacklogsCount() {
        return backlogsCount;
    }

    public void setBacklogsCount(int backlogsCount) {
        if (backlogsCount < 0) throw new IllegalArgumentException("Backlogs count cannot be negative");
        this.backlogsCount = backlogsCount;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
}
