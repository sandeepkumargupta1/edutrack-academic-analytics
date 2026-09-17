package com.edutrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Value object representing an analytical risk evaluation result.
 */
public class RiskAssessment implements Serializable {
    private static final long serialVersionUID = 1L;

    private final RiskLevel riskLevel;
    private final double riskScore;           // 0.0 to 100.0
    private final double failureProbability;  // 0.0 to 1.0
    private final double projectedFinalScore; // 0.0 to 100.0
    private final List<String> keyRiskFactors;
    private final String recommendation;
    private final LocalDateTime assessedAt;

    public RiskAssessment(RiskLevel riskLevel, double riskScore, double failureProbability,
                          double projectedFinalScore, List<String> keyRiskFactors, String recommendation) {
        this.riskLevel = riskLevel != null ? riskLevel : RiskLevel.LOW;
        this.riskScore = Math.max(0.0, Math.min(100.0, riskScore));
        this.failureProbability = Math.max(0.0, Math.min(1.0, failureProbability));
        this.projectedFinalScore = Math.max(0.0, Math.min(100.0, projectedFinalScore));
        this.keyRiskFactors = keyRiskFactors != null ? new ArrayList<>(keyRiskFactors) : new ArrayList<>();
        this.recommendation = recommendation != null ? recommendation : "Maintain current academic trajectory.";
        this.assessedAt = LocalDateTime.now();
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public double getRiskScore() {
        return riskScore;
    }

    public double getFailureProbability() {
        return failureProbability;
    }

    public double getProjectedFinalScore() {
        return projectedFinalScore;
    }

    public List<String> getKeyRiskFactors() {
        return Collections.unmodifiableList(keyRiskFactors);
    }

    public String getRecommendation() {
        return recommendation;
    }

    public LocalDateTime getAssessedAt() {
        return assessedAt;
    }

    public String getFormattedAssessedAt() {
        return assessedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("[%s] Failure Probability: %.1f%%, Projected Final: %.1f/100",
                riskLevel.getLabel(), failureProbability * 100.0, projectedFinalScore);
    }
}
