package com.edutrack.strategy;

import com.edutrack.model.AcademicRecord;
import com.edutrack.model.Intervention;
import com.edutrack.model.RiskLevel;
import com.edutrack.model.Student;

/**
 * Concrete strategy pairing students with high-performing student mentors for peer support.
 */
public class PeerTutoringStrategy implements InterventionStrategy {

    @Override
    public String getStrategyName() {
        return "Peer Tutoring & Study Group Pairing";
    }

    @Override
    public boolean isApplicable(Student student) {
        AcademicRecord rec = student.getAcademicRecord();
        if (rec == null) return false;
        boolean isModerate = student.getRiskAssessment() != null &&
                student.getRiskAssessment().getRiskLevel() == RiskLevel.MODERATE;
        return isModerate || rec.getBacklogsCount() > 0 || rec.getStudyHoursPerWeek() < 8.0;
    }

    @Override
    public Intervention createIntervention(Student student, String interventionId) {
        AcademicRecord rec = student.getAcademicRecord();
        String notes = String.format("Paired with high-CGPA peer mentor. Active backlogs: %d, Study hours: %.1f hrs/week. Weekly progress checkpoint every Friday.",
                rec.getBacklogsCount(), rec.getStudyHoursPerWeek());
        return new Intervention(
            interventionId,
            student.getRegNumber(),
            student.getName(),
            "Peer Mentorship & Collaborative Learning Circle",
            getStrategyName(),
            student.getMentorName(),
            notes
        );
    }
}
