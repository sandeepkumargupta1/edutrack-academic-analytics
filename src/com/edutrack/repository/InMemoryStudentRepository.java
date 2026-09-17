package com.edutrack.repository;

import com.edutrack.model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * High-performance in-memory repository implementing StudentRepository.
 * Provides O(1) key lookups and thread-safe data structures.
 */
public class InMemoryStudentRepository implements StudentRepository {
    protected final Map<String, Student> studentStore = new ConcurrentHashMap<>();

    @Override
    public Student save(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        studentStore.put(student.getRegNumber().toUpperCase(), student);
        return student;
    }

    @Override
    public Optional<Student> findByRegNumber(String regNumber) {
        if (regNumber == null) return Optional.empty();
        return Optional.ofNullable(studentStore.get(regNumber.trim().toUpperCase()));
    }

    @Override
    public List<Student> findAll() {
        List<Student> list = new ArrayList<>(studentStore.values());
        Collections.sort(list);
        return list;
    }

    @Override
    public boolean deleteByRegNumber(String regNumber) {
        if (regNumber == null) return false;
        return studentStore.remove(regNumber.trim().toUpperCase()) != null;
    }

    @Override
    public boolean existsByRegNumber(String regNumber) {
        if (regNumber == null) return false;
        return studentStore.containsKey(regNumber.trim().toUpperCase());
    }

    @Override
    public int count() {
        return studentStore.size();
    }

    @Override
    public void flush() {
        // No-op for purely in-memory store
    }
}
