package com.edutrack.repository;

import com.edutrack.model.Student;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for Student entity operations.
 * Demonstrates OOP Abstraction and Interface Segregation Principle.
 */
public interface StudentRepository {
    /**
     * Persists or updates a student in the store.
     */
    Student save(Student student);

    /**
     * Looks up a student by unique registration number.
     */
    Optional<Student> findByRegNumber(String regNumber);

    /**
     * Retrieves all students in the repository.
     */
    List<Student> findAll();

    /**
     * Removes a student by registration number.
     */
    boolean deleteByRegNumber(String regNumber);

    /**
     * Checks if a student exists by registration number.
     */
    boolean existsByRegNumber(String regNumber);

    /**
     * Total count of students.
     */
    int count();

    /**
     * Persists in-memory records to storage.
     */
    void flush();
}
