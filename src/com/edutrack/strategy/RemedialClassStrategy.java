package com.edutrack.strategy;

import com.edutrack.model.AcademicRecord;
import com.edutrack.model.Intervention;
import com.edutrack.model.Student;

/**
 * Concrete strategy generating remedial instructional sessions for students failing internal assessments.
 */
public class RemedialClassStrategy implements InterventionStrategy {

    @Override
    public String getStrategyName() {
        return "Remedial Coaching & Doubt Clearing";
    }

    @Override
    public boolean isApplicable(Student student) {
        AcademicRecord rec = student.getAcademicRecord();
        if (rec == null) return false;
        return rec.getInternalTest1() < 22.0 || rec.getInternalTest2() < 22.0 || rec.calculateCompositeScore() < 50.0;
    }

    @Override
    public Intervention createIntervention(Student student, String interventionId) {
        AcademicRecord rec = student.getAcademicRecord();
        String notes = String.format("Assigned 8 hours of targeted subject tutorials. IT1: %.1f/50, IT2: %.1f/50. Focus on core problem-solving modules.",
                rec.getInternalTest1(), rec.getInternalTest2());
        return new Intervention(
            interventionId,
            student.getRegNumber(),
            student.getName(),
            "Mandatory Remedial Classes in " + (rec.getCourseCode()),
            getStrategyName(),
            student.getMentorName(),
            notes
        );
    }
}
