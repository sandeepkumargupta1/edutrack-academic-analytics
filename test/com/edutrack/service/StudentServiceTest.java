package com.edutrack.service;

import com.edutrack.TestRunner.Assertion;
import com.edutrack.exception.DuplicateRecordException;
import com.edutrack.exception.StudentNotFoundException;
import com.edutrack.model.AcademicRecord;
import com.edutrack.model.Student;
import com.edutrack.repository.InMemoryStudentRepository;

import java.util.List;

public class StudentServiceTest {

    public void testCreateAndRetrieveStudent() throws Exception {
        StudentService service = new StudentService(new InMemoryStudentRepository());
        Student s = service.createStudent("23BCE9999", "Test Student", "test@vit.ac.in", "CSE", 5, 8.5, "Dr. Guide");
        Assertion.assertNotNull(s, "Student should be created");

        Student fetched = service.getStudent("23BCE9999");
        Assertion.assertEquals("Test Student", fetched.getName(), "Name should match");
        Assertion.assertEquals(8.5, fetched.getCgpa(), 0.001, "CGPA should match");
    }

    public void testDuplicateRegistrationRejection() throws Exception {
        StudentService service = new StudentService(new InMemoryStudentRepository());
        service.createStudent("23BCE9999", "Student One", "one@vit.ac.in", "CSE", 5, 8.0, "Dr. Guide");

        boolean caught = false;
        try {
            service.createStudent("23BCE9999", "Student Two", "two@vit.ac.in", "CSE", 5, 7.5, "Dr. Guide");
        } catch (DuplicateRecordException e) {
            caught = true;
        }
        Assertion.assertTrue(caught, "Creating duplicate student registration number must throw DuplicateRecordException");
    }

    public void testUpdateAcademicRecord() throws Exception {
        StudentService service = new StudentService(new InMemoryStudentRepository());
        service.createStudent("23BCE9999", "Test Student", "test@vit.ac.in", "CSE", 5, 8.5, "Dr. Guide");

        AcademicRecord rec = new AcademicRecord("CSE2001", 45.0, 42.0, 18.0, 26.0, 16.0, 92.0, 14.0, 0);
        service.updateAcademicRecord("23BCE9999", rec);

        Student updated = service.getStudent("23BCE9999");
        Assertion.assertNotNull(updated.getAcademicRecord(), "Academic record should be attached");
        Assertion.assertEquals(45.0, updated.getAcademicRecord().getInternalTest1(), 0.01, "IT1 score should match");
    }

    public void testSearchStudents() throws Exception {
        StudentService service = new StudentService(new InMemoryStudentRepository());
        service.createStudent("23BCE1001", "Kavya Sharma", "kavya@vit.ac.in", "CSE", 5, 8.9, "Dr. Raman");
        service.createStudent("23BCE1002", "Arjun Nair", "arjun@vit.ac.in", "IT", 5, 7.8, "Dr. Sundar");

        List<Student> results = service.searchStudents("kavya");
        Assertion.assertEquals(1, results.size(), "Search should find 1 student matching 'kavya'");
        Assertion.assertEquals("23BCE1001", results.get(0).getRegNumber(), "Reg number should match");
    }

    public void testDeleteStudent() throws Exception {
        StudentService service = new StudentService(new InMemoryStudentRepository());
        service.createStudent("23BCE1001", "Kavya", "k@vit.ac.in", "CSE", 5, 8.0, "Dr. Raman");
        Assertion.assertEquals(1, service.getTotalCount(), "Count should be 1");

        boolean deleted = service.deleteStudent("23BCE1001");
        Assertion.assertTrue(deleted, "Delete should return true");
        Assertion.assertEquals(0, service.getTotalCount(), "Count should be 0");
    }

    public void testNonExistentStudentThrowsException() {
        StudentService service = new StudentService(new InMemoryStudentRepository());
        boolean caught = false;
        try {
            service.getStudent("NONEXISTENT");
        } catch (StudentNotFoundException e) {
            caught = true;
        }
        Assertion.assertTrue(caught, "Fetching non-existent student must throw StudentNotFoundException");
    }
}
