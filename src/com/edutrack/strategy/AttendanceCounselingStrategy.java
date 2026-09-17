package com.edutrack.strategy;

import com.edutrack.model.AcademicRecord;
import com.edutrack.model.Intervention;
import com.edutrack.model.Student;

/**
 * Concrete strategy addressing chronic absenteeism and attendance shortages.
 */
public class AttendanceCounselingStrategy implements InterventionStrategy {

    @Override
    public String getStrategyName() {
        return "Attendance Shortage Counseling & Advisory";
    }

    @Override
    public boolean isApplicable(Student student) {
        AcademicRecord rec = student.getAcademicRecord();
        if (rec == null) return false;
        return rec.isAttendanceDeficient();
    }

    @Override
    public Intervention createIntervention(Student student, String interventionId) {
        AcademicRecord rec = student.getAcademicRecord();
        String notes = String.format("Attendance currently at %.1f%% (Mandatory threshold is 75.0%%). Student advised regarding detention risks and required make-up sessions.",
                rec.getAttendancePercentage());
        return new Intervention(
            interventionId,
            student.getRegNumber(),
            student.getName(),
            "Urgent Attendance Advisory & Parental Notification",
            getStrategyName(),
            student.getMentorName(),
            notes
        );
    }
}
