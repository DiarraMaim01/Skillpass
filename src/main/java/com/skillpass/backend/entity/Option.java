package com.skillpass.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

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
    @JoinColumn(name = "question_id")
    private Question question;
}
