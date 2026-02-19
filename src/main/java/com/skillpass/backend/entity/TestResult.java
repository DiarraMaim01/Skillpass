package com.skillpass.backend.entity;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @ToString.Exclude
    private Test test;

    // Calcul du pourcentage
    public double getPercentage() {
        if (totalQuestions == 0) return 0;
        return (score * 100.0) / totalQuestions;
    }

    // Note sur 20
    public double getScoreOnTwenty() {
        if (totalQuestions == 0) return 0;
        return (score * 20.0) / totalQuestions;
    }
}