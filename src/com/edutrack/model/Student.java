package com.edutrack.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Core domain aggregate entity representing an enrolled student.
 * Encapsulates demographic data, continuous academic performance records,
 * risk evaluations, and assigned interventions.
 */
public class Student implements Serializable, Comparable<Student> {
    private static final long serialVersionUID = 1L;

    private final String regNumber;
    private String name;
    private String email;
    private String department;
    private int semester;
    private double cgpa;
    private String mentorName;

    private AcademicRecord academicRecord;
    private RiskAssessment riskAssessment;
    private final List<Intervention> interventions = new ArrayList<>();

    public Student(String regNumber, String name, String email, String department, int semester, double cgpa, String mentorName) {
        if (regNumber == null || regNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        this.regNumber = regNumber.trim().toUpperCase();
        this.name = name.trim();
        this.email = email != null ? email.trim() : "";
        this.department = department != null ? department.trim() : "Computer Science & Engineering";
        this.semester = Math.max(1, Math.min(10, semester));
        this.cgpa = Math.max(0.0, Math.min(10.0, cgpa));
        this.mentorName = mentorName != null ? mentorName.trim() : "Faculty Advisor";
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        this.name = name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = Math.max(1, Math.min(10, semester));
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = Math.max(0.0, Math.min(10.0, cgpa));
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public AcademicRecord getAcademicRecord() {
        return academicRecord;
    }

    public void setAcademicRecord(AcademicRecord academicRecord) {
        this.academicRecord = academicRecord;
    }

    public RiskAssessment getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(RiskAssessment riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public List<Intervention> getInterventions() {
        return Collections.unmodifiableList(interventions);
    }

    public void addIntervention(Intervention intervention) {
        if (intervention != null) {
            this.interventions.add(intervention);
        }
    }

    @Override
    public int compareTo(Student o) {
        return this.regNumber.compareTo(o.regNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(regNumber, student.regNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (CGPA: %.2f)", regNumber, name, cgpa);
    }
}
