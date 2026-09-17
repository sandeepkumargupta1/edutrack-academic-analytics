package com.edutrack.service;

import com.edutrack.model.*;
import com.edutrack.strategy.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service managing academic interventions using the Strategy Pattern.
 */
public class InterventionService {

    private final List<InterventionStrategy> strategies = new ArrayList<>();
    private final Map<String, Intervention> interventionRegistry = new ConcurrentHashMap<>();
    private final AtomicInteger idSequence = new AtomicInteger(1001);

    public InterventionService() {
        registerStrategy(new RemedialClassStrategy());
        registerStrategy(new AttendanceCounselingStrategy());
        registerStrategy(new PeerTutoringStrategy());
    }

    public void registerStrategy(InterventionStrategy strategy) {
        if (strategy != null) {
            strategies.add(strategy);
        }
    }

    public List<Intervention> generateInterventionsForStudent(Student student) {
        List<Intervention> generated = new ArrayList<>();
        if (student == null) return generated;

        for (InterventionStrategy strategy : strategies) {
            if (strategy.isApplicable(student)) {
                // Check if identical active intervention already exists for this student
                boolean exists = student.getInterventions().stream()
                        .anyMatch(i -> i.getStrategyName().equals(strategy.getStrategyName())
                                && i.getStatus() != InterventionStatus.RESOLVED);

                if (!exists) {
                    String id = "INT-" + idSequence.getAndIncrement();
                    Intervention intervention = strategy.createIntervention(student, id);
                    student.addIntervention(intervention);
                    interventionRegistry.put(id, intervention);
                    generated.add(intervention);
                }
            }
        }
        return generated;
    }

    public int generateBatchInterventions(List<Student> students) {
        int totalNew = 0;
        if (students != null) {
            for (Student s : students) {
                totalNew += generateInterventionsForStudent(s).size();
            }
        }
        return totalNew;
    }

    public List<Intervention> getAllInterventions() {
        List<Intervention> list = new ArrayList<>(interventionRegistry.values());
        list.sort(Comparator.comparing(Intervention::getCreatedAt).reversed());
        return list;
    }

    public Optional<Intervention> getInterventionById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(interventionRegistry.get(id.trim().toUpperCase()));
    }

    public boolean updateStatus(String interventionId, InterventionStatus newStatus, String notes) {
        Optional<Intervention> opt = getInterventionById(interventionId);
        if (opt.isPresent()) {
            Intervention i = opt.get();
            i.setStatus(newStatus);
            if (notes != null && !notes.trim().isEmpty()) {
                i.setNotes(i.getNotes() + " | Update: " + notes.trim());
            }
            return true;
        }
        return false;
    }
}
