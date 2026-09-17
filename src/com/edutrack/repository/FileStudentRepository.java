package com.edutrack.repository;

import com.edutrack.model.Student;
import com.edutrack.util.CsvHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * File-backed repository persisting Student data to CSV format.
 * Loads from working database or seeds automatically if database is not yet initialized.
 */
public class FileStudentRepository extends InMemoryStudentRepository {

    private final File storageFile;
    private final File seedFile;

    public FileStudentRepository(File storageFile, File seedFile) {
        this.storageFile = storageFile;
        this.seedFile = seedFile;
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            if (storageFile.exists() && storageFile.length() > 0) {
                List<Student> loaded = CsvHandler.loadStudents(storageFile);
                for (Student s : loaded) {
                    studentStore.put(s.getRegNumber().toUpperCase(), s);
                }
            } else if (seedFile != null && seedFile.exists()) {
                List<Student> seeded = CsvHandler.loadStudents(seedFile);
                for (Student s : seeded) {
                    studentStore.put(s.getRegNumber().toUpperCase(), s);
                }
                flush(); // Save into working db
            }
        } catch (IOException e) {
            System.err.println("Error initializing FileStudentRepository: " + e.getMessage());
        }
    }

    @Override
    public Student save(Student student) {
        Student saved = super.save(student);
        flush();
        return saved;
    }

    @Override
    public boolean deleteByRegNumber(String regNumber) {
        boolean deleted = super.deleteByRegNumber(regNumber);
        if (deleted) flush();
        return deleted;
    }

    @Override
    public void flush() {
        try {
            CsvHandler.saveStudents(storageFile, findAll());
        } catch (IOException e) {
            System.err.println("Error saving students to " + storageFile.getPath() + ": " + e.getMessage());
        }
    }
}
