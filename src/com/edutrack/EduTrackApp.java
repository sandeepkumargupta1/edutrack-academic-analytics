package com.edutrack;

import com.edutrack.cli.CliController;
import com.edutrack.repository.FileStudentRepository;
import com.edutrack.repository.StudentRepository;
import com.edutrack.service.AnalyticsEngine;
import com.edutrack.service.AuthService;
import com.edutrack.service.InterventionService;
import com.edutrack.service.StudentService;

import java.io.File;

/**
 * Main Application Entrypoint for EduTrack CLI System.
 * Supports both interactive terminal mode and headless evaluation mode.
 */
public class EduTrackApp {

    public static void main(String[] args) {
        // Initialize file storage and repositories
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File dbFile = new File(dataDir, "students_db.csv");
        File seedFile = new File(dataDir, "students_seed.csv");

        StudentRepository repository = new FileStudentRepository(dbFile, seedFile);
        StudentService studentService = new StudentService(repository);
        AnalyticsEngine analyticsEngine = new AnalyticsEngine();
        InterventionService interventionService = new InterventionService();
        AuthService authService = new AuthService();

        CliController controller = new CliController(studentService, analyticsEngine, interventionService, authService);

        // Check for headless evaluation arguments
        boolean isHeadless = false;
        if (args != null && args.length > 0) {
            for (String arg : args) {
                if ("--report".equalsIgnoreCase(arg) || "--batch".equalsIgnoreCase(arg) || "-r".equalsIgnoreCase(arg)) {
                    isHeadless = true;
                    break;
                }
            }
        }

        if (isHeadless) {
            controller.executeHeadlessReport();
        } else {
            controller.start();
        }
    }
}
