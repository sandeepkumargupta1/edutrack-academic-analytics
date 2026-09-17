package com.edutrack.service;

import com.edutrack.TestRunner.Assertion;
import com.edutrack.model.*;

import java.util.List;

public class InterventionServiceTest {

    public void testRemedialInterventionGeneratedForLowInternals() {
        InterventionService service = new InterventionService();
        Student s = new Student("23BCE1005", "Struggling", "s@vit.ac.in", "CSE", 5, 5.5, "Dr. Raman");
        // Low IT1 = 15.0, Low IT2 = 14.0
        s.setAcademicRecord(new AcademicRecord("CSE2001", 15.0, 14.0, 10.0, 15.0, 8.0, 85.0, 10.0, 0));

        List<Intervention> generated = service.generateInterventionsForStudent(s);
        Assertion.assertTrue(generated.size() >= 1, "Should generate at least 1 intervention");

        boolean hasRemedial = generated.stream().anyMatch(i -> i.getStrategyName().contains("Remedial"));
        Assertion.assertTrue(hasRemedial, "Must trigger Remedial Coaching strategy for failing internal tests");
    }

    public void testAttendanceInterventionGeneratedForDeficientAttendance() {
        InterventionService service = new InterventionService();
        Student s = new Student("23BCE1007", "Absentee", "a@vit.ac.in", "CSE", 5, 6.5, "Dr. Raman");
        // Good internals but attendance 55%
        s.setAcademicRecord(new AcademicRecord("CSE2001", 35.0, 35.0, 15.0, 22.0, 15.0, 55.0, 10.0, 0));

        List<Intervention> generated = service.generateInterventionsForStudent(s);
        boolean hasAttendance = generated.stream().anyMatch(i -> i.getStrategyName().contains("Attendance"));
        Assertion.assertTrue(hasAttendance, "Must trigger Attendance Counseling strategy for attendance < 75%");
    }

    public void testUpdateInterventionStatus() {
        InterventionService service = new InterventionService();
        Student s = new Student("23BCE1005", "Struggling", "s@vit.ac.in", "CSE", 5, 5.5, "Dr. Raman");
        s.setAcademicRecord(new AcademicRecord("CSE2001", 15.0, 14.0, 8.0, 12.0, 6.0, 50.0, 3.0, 2));

        List<Intervention> generated = service.generateInterventionsForStudent(s);
        Assertion.assertTrue(!generated.isEmpty(), "Generated list not empty");

        String id = generated.get(0).getId();
        boolean updated = service.updateStatus(id, InterventionStatus.RESOLVED, "Student attended tutorial series");
        Assertion.assertTrue(updated, "Status update should succeed");

        Intervention item = service.getInterventionById(id).orElse(null);
        Assertion.assertNotNull(item, "Item must exist");
        Assertion.assertEquals(InterventionStatus.RESOLVED, item.getStatus(), "Status should be RESOLVED");
        Assertion.assertNotNull(item.getResolvedAt(), "Resolved timestamp must be recorded");
    }
}
