package com.edutrack.util;

import com.edutrack.TestRunner.Assertion;
import com.edutrack.model.AcademicRecord;
import com.edutrack.model.Student;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvHandlerTest {

    public void testSaveAndLoadRoundtrip() throws IOException {
        File tempFile = File.createTempFile("test_students_", ".csv");
        tempFile.deleteOnExit();

        List<Student> original = new ArrayList<>();
        Student s1 = new Student("23BCE5001", "Test Alpha", "alpha@vit.ac.in", "CSE", 5, 8.4, "Dr. Mentor");
        s1.setAcademicRecord(new AcademicRecord("CSE2001", 42.0, 41.0, 18.0, 26.0, 17.0, 91.0, 15.0, 0));

        Student s2 = new Student("23BCE5002", "Test Beta", "beta@vit.ac.in", "IT", 5, 6.2, "Dr. Guide");
        s2.setAcademicRecord(new AcademicRecord("CSE2001", 20.0, 22.0, 11.0, 16.0, 9.0, 65.0, 5.0, 2));

        original.add(s1);
        original.add(s2);

        CsvHandler.saveStudents(tempFile, original);
        Assertion.assertTrue(tempFile.exists() && tempFile.length() > 0, "Saved file should exist and not be empty");

        List<Student> loaded = CsvHandler.loadStudents(tempFile);
        Assertion.assertEquals(2, loaded.size(), "Loaded list should have 2 students");

        Student l1 = loaded.get(0);
        Assertion.assertEquals("23BCE5001", l1.getRegNumber(), "Reg number should match");
        Assertion.assertEquals("Test Alpha", l1.getName(), "Name should match");
        Assertion.assertEquals(8.4, l1.getCgpa(), 0.001, "CGPA should match");
        Assertion.assertNotNull(l1.getAcademicRecord(), "Academic record should be populated");
        Assertion.assertEquals(42.0, l1.getAcademicRecord().getInternalTest1(), 0.001, "IT1 score should match");
        Assertion.assertEquals(91.0, l1.getAcademicRecord().getAttendancePercentage(), 0.001, "Attendance should match");
    }
}
