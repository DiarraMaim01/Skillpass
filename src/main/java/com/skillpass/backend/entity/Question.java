package com.skillpass.backend.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String contenu;

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

    // Énumération pour les catégories
    public enum Categorie {
        JAVA , SQL, JAVASCRIPT, SPRING, DOCKER, GIT
    }

    //relation avec Option
    @OneToMany( mappedBy = "question", cascade = CascadeType.ALL) // mappedBy permet d'utiliser la relation deja etabli dans option
    @ToString.Exclude
    private List<Option> options = new ArrayList<Option>();

    //relation avec Test
    @ManyToMany(mappedBy = "questions")
    @ToString.Exclude
    private List<Test> tests;


    // Méthodes utilitaires pour la relation avec Option

    public void addOption(Option option) {
        if (options == null) {
            options = new ArrayList<>();
        }
        options.add(option);
        option.setQuestion(this);
    }

    public void removeOption(Option option) {
        if (options != null) {
            options.remove(option);
            option.setQuestion(null);
        }
    }

    // Méthode pour créer une option rapidement
    public Option createOption(String texte, boolean correcte) {
        Option option = new Option();
        option.setTexte(texte);
        option.setCorrecte(correcte);
        this.addOption(option);
        return option;
    }




}
