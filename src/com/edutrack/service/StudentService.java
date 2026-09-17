package com.edutrack.service;

import com.edutrack.exception.DuplicateRecordException;
import com.edutrack.exception.InvalidAcademicRecordException;
import com.edutrack.exception.StudentNotFoundException;
import com.edutrack.model.AcademicRecord;
import com.edutrack.model.Student;
import com.edutrack.repository.StudentRepository;
import com.edutrack.util.CsvHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service orchestrating student domain operations, input validations, and persistence.
 */
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudent(String regNumber) throws StudentNotFoundException {
        return repository.findByRegNumber(regNumber)
                .orElseThrow(() -> new StudentNotFoundException(regNumber));
    }

    public Student createStudent(String regNumber, String name, String email, String dept, int sem, double cgpa, String mentor)
            throws DuplicateRecordException {
        if (repository.existsByRegNumber(regNumber)) {
            throw new DuplicateRecordException("Student with registration number " + regNumber + " already exists.");
        }
        Student student = new Student(regNumber, name, email, dept, sem, cgpa, mentor);
        return repository.save(student);
    }

    public void updateAcademicRecord(String regNumber, AcademicRecord record)
            throws StudentNotFoundException, InvalidAcademicRecordException {
        Student student = getStudent(regNumber);
        if (record == null) {
            throw new InvalidAcademicRecordException("Academic record cannot be null.");
        }
        student.setAcademicRecord(record);
        repository.save(student);
    }

    public boolean deleteStudent(String regNumber) throws StudentNotFoundException {
        if (!repository.existsByRegNumber(regNumber)) {
            throw new StudentNotFoundException(regNumber);
        }
        return repository.deleteByRegNumber(regNumber);
    }

    public List<Student> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }
        String term = keyword.trim().toLowerCase();
        return repository.findAll().stream()
                .filter(s -> s.getRegNumber().toLowerCase().contains(term)
                        || s.getName().toLowerCase().contains(term)
                        || s.getDepartment().toLowerCase().contains(term)
                        || s.getMentorName().toLowerCase().contains(term))
                .collect(Collectors.toList());
    }

    public int importCsv(File file) throws IOException {
        List<Student> imported = CsvHandler.loadStudents(file);
        for (Student s : imported) {
            repository.save(s);
        }
        return imported.size();
    }

    public void exportCsv(File file) throws IOException {
        CsvHandler.saveStudents(file, repository.findAll());
    }

    public int getTotalCount() {
        return repository.count();
    }
}
