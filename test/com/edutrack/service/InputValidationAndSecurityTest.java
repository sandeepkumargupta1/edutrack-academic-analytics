package com.edutrack.service;

import com.edutrack.TestRunner.Assertion;
import com.edutrack.cli.InputValidator;
import com.edutrack.model.*;

import java.util.List;

/**
 * Unit tests validating input boundary constraints, exception propagation,
 * and Role-Based Access Control (RBAC) capability segregation.
 */
public class InputValidationAndSecurityTest {

    public void testNegativeInternalMarksRejected() {
        boolean caught = false;
        try {
            new AcademicRecord("CSE2001", -5.0, 30.0, 15.0, 20.0, 15.0, 85.0, 10.0, 0);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        Assertion.assertTrue(caught, "Negative Internal Test 1 score must be rejected with IllegalArgumentException");
    }

    public void testExcessiveInternalMarksRejected() {
        boolean caught = false;
        try {
            new AcademicRecord("CSE2001", 55.0, 30.0, 15.0, 20.0, 15.0, 85.0, 10.0, 0);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        Assertion.assertTrue(caught, "Internal Test 1 score > 50 must be rejected with IllegalArgumentException");
    }

    public void testAttendanceOutOfBoundsRejected() {
        boolean caughtUnder = false;
        try {
            new AcademicRecord("CSE2001", 30.0, 30.0, 15.0, 20.0, 15.0, -1.0, 10.0, 0);
        } catch (IllegalArgumentException e) {
            caughtUnder = true;
        }
        Assertion.assertTrue(caughtUnder, "Negative attendance percentage must be rejected");

        boolean caughtOver = false;
        try {
            new AcademicRecord("CSE2001", 30.0, 30.0, 15.0, 20.0, 15.0, 105.0, 10.0, 0);
        } catch (IllegalArgumentException e) {
            caughtOver = true;
        }
        Assertion.assertTrue(caughtOver, "Attendance percentage > 100 must be rejected");
    }

    public void testNegativeStudyHoursAndBacklogsRejected() {
        boolean caughtHours = false;
        try {
            new AcademicRecord("CSE2001", 30.0, 30.0, 15.0, 20.0, 15.0, 80.0, -2.0, 0);
        } catch (IllegalArgumentException e) {
            caughtHours = true;
        }
        Assertion.assertTrue(caughtHours, "Negative study hours must be rejected");

        boolean caughtBacklogs = false;
        try {
            new AcademicRecord("CSE2001", 30.0, 30.0, 15.0, 20.0, 15.0, 80.0, 10.0, -1);
        } catch (IllegalArgumentException e) {
            caughtBacklogs = true;
        }
        Assertion.assertTrue(caughtBacklogs, "Negative backlogs count must be rejected");
    }

    public void testRoleCapabilitySegregation() {
        User student = new StudentUser("test_std", "hash", "Student Name", "s@vit.ac.in", "23BCE1001");
        List<String> stdCaps = student.getDashboardCapabilities();
        Assertion.assertTrue(stdCaps.contains("VIEW_SELF_ACADEMICS"), "Student must be permitted to view self academics");
        Assertion.assertFalse(stdCaps.contains("UPDATE_MARKS"), "Student must NOT have permission to update marks");
        Assertion.assertFalse(stdCaps.contains("TRIGGER_INTERVENTIONS"), "Student must NOT have permission to trigger interventions");

        User faculty = new FacultyUser("test_fac", "hash", "Faculty Name", "f@vit.ac.in", "CSE", "FAC001");
        List<String> facCaps = faculty.getDashboardCapabilities();
        Assertion.assertTrue(facCaps.contains("VIEW_STUDENTS"), "Faculty must have VIEW_STUDENTS");
        Assertion.assertTrue(facCaps.contains("UPDATE_MARKS"), "Faculty must have UPDATE_MARKS");
        Assertion.assertTrue(facCaps.contains("TRIGGER_INTERVENTIONS"), "Faculty must have TRIGGER_INTERVENTIONS");
        Assertion.assertFalse(facCaps.contains("SYSTEM_MAINTENANCE"), "Faculty must NOT have SYSTEM_MAINTENANCE");

        User admin = new AdminUser("test_adm", "hash", "Admin Name", "a@vit.ac.in");
        List<String> admCaps = admin.getDashboardCapabilities();
        Assertion.assertTrue(admCaps.contains("MANAGE_USERS"), "Admin must have MANAGE_USERS");
        Assertion.assertTrue(admCaps.contains("SYSTEM_MAINTENANCE"), "Admin must have SYSTEM_MAINTENANCE");
    }

    public void testInputValidatorRegNoAndEmail() {
        Assertion.assertTrue(InputValidator.isValidRegNumber("23BCE1001"), "Valid registration number must pass");
        Assertion.assertTrue(InputValidator.isValidRegNumber("22BIT0045"), "Valid IT registration number must pass");
        Assertion.assertFalse(InputValidator.isValidRegNumber("12345"), "Invalid registration number must fail");
        Assertion.assertFalse(InputValidator.isValidRegNumber("BCE1001"), "Missing year prefix must fail");
        Assertion.assertFalse(InputValidator.isValidRegNumber(null), "Null registration number must fail");

        Assertion.assertTrue(InputValidator.isValidEmail("student@vitstudent.ac.in"), "Valid email must pass");
        Assertion.assertFalse(InputValidator.isValidEmail("invalid-email"), "Malformed email must fail");
        Assertion.assertFalse(InputValidator.isValidEmail(null), "Null email must fail");
    }

    public void testCustomExceptionsThrownForInvalidInputs() {
        boolean caughtMarks = false;
        try {
            InputValidator.validateMarks(-2.0, 30.0, 15.0, 20.0, 15.0);
        } catch (com.edutrack.exception.InvalidMarksException e) {
            caughtMarks = true;
        }
        Assertion.assertTrue(caughtMarks, "validateMarks must throw InvalidMarksException for negative marks");

        boolean caughtStudentData = false;
        try {
            InputValidator.validateStudentProfile("23BCE1001", "", "s@vit.ac.in", 5, 8.0);
        } catch (com.edutrack.exception.InvalidStudentDataException e) {
            caughtStudentData = true;
        }
        Assertion.assertTrue(caughtStudentData, "validateStudentProfile must throw InvalidStudentDataException for empty name");
    }

    public void testTableRendererOverloadedTitle() {
        List<String> headers = java.util.Arrays.asList("Col 1", "Col 2");
        List<List<String>> rows = java.util.Collections.singletonList(java.util.Arrays.asList("A", "B"));
        String output = com.edutrack.util.TableRenderer.renderTable("TEST TITLE", headers, rows);
        Assertion.assertTrue(output.contains("TEST TITLE"), "Output must contain title");
        Assertion.assertTrue(output.contains("Col 1"), "Output must contain header");
    }
}


