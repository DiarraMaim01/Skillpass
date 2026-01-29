package com.skillpass.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Table (name = "options")
@Data

public class Option {

    @Id
    @GeneratedValue( strategy =GenerationType.IDENTITY)
    private long id ;

    @Column(nullable = false, length= 1000)
    private String texte;

    private boolean correcte =false ;

    // Relations
    @ManyToOne // je suis un enfant de la table question / plusieurs options par question
    @JoinColumn(name = "question_id") // crée la colonne question_id en base
    @ToString.Exclude
    private Question question;

    // Méthode utilitaires
    public void marquerCommeCorrecte() {
        this.correcte = true;
    }

    public boolean estCorrecte() {
        return this.correcte;
    }
}
