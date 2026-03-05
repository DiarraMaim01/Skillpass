package com.skillpass.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
@Data
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;
    private int totalQuestions;
    private int correctAnswers;

    private LocalDateTime completedAt = LocalDateTime.now();

    private int timeSpentSeconds;
    private int totalPoints;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @JsonIgnoreProperties({"password", "enabled", "createdAt", "lastLogin", "role"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @ToString.Exclude
    @JsonIgnoreProperties({"results", "questions", "dureeEstimeeMinutes", "nombreQuestions"})
    private Test test;

    // Calcul du pourcentage
    public double getPercentage() {
        if (totalPoints == 0) return 0;
        return (score * 100.0) / totalPoints;
    }

    // Note sur 20
    public double getScoreOnTwenty() {
        if (totalQuestions == 0) return 0;
        return (score * 20.0) / totalQuestions;
    }
}