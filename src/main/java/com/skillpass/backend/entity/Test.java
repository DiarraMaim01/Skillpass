package com.skillpass.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "tests")
@Data

public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titre;

    @Column(length=1000)
    private String description;

    private Integer dureeMinutes = 30;

    //Relation avec Question
    @ManyToMany
    @JoinTable( // table de jointure
            name = "test_questions", //nom de la table
            joinColumns = @JoinColumn (name = "test_id"), // la colonne test_id reference id de Test
            inverseJoinColumns = @JoinColumn (name = "question_id") // la colle question_id reference Question
    )
    @ToString.Exclude
    private List<Question> questions;

    //relation avec testResult

    @OneToMany(mappedBy = "test")
    @ToString.Exclude
    private List<TestResult> results = new ArrayList<>();

    // Méthodes utilitaires pour la relation avec Question

    public void addQuestion(Question question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        questions.add(question);

        if (question.getTests() == null) {
            question.setTests(new ArrayList<>());
        }
        question.getTests().add(this);
    }

    public void removeQuestion(Question question) {
        if (questions != null) {
            questions.remove(question);

            if (question.getTests() != null) {
                question.getTests().remove(this);
            }
        }
    }


    public int getNombreQuestions() {
        return (questions != null) ? questions.size() : 0;
    }


    public int getDureeEstimeeMinutes() {
        return (questions != null) ? questions.size() * 2 : 0; // 2 min par question
    }

}
