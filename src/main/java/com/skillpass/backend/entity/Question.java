package com.skillpass.backend.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Data
public class Question {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY) // auto incrementation
    private long id;

    @Column(nullable = false)
    private String titre;

    @Column(length = 1000)
    public String contenu;

    @Enumerated(EnumType.STRING)

    private Categorie categorie ;

    @Enumerated(EnumType.STRING)
    private Niveau niveau ;

    // Énumération pour les niveaux
    public enum Niveau {
        DÉBUTANT, INTERMÉDIAIRE, EXPERT
    }

    private Integer points = 5;

    @Column(name = "created_at")
    private LocalDateTime created_at = LocalDateTime.now();


    public enum Categorie {
        JAVA , SQL, JAVASCRIPT, SPRING, DOCKER, GIT
    }
}
