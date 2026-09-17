package com.edutrack.strategy;

import com.edutrack.model.Intervention;
import com.edutrack.model.Student;

/**
 * Strategy pattern interface for generating personalized academic interventions.
 * Demonstrates OOP Polymorphism and Open-Closed Principle.
 */
public interface InterventionStrategy {
    /**
     * Name of the intervention strategy.
     */
    String getStrategyName();

    /**
     * Determines whether this strategy applies to the given student.
     */
    boolean isApplicable(Student student);

    /**
     * Instantiates an actionable intervention for the student.
     */
    Intervention createIntervention(Student student, String interventionId);
}
