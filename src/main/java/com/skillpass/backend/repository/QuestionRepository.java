package com.skillpass.backend.repository;

import com.skillpass.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    //trouver par categorie
    List<Question> findByCategorie (Question.Categorie categorie);

    //trouver par niveau
    List<Question> findByNiveau (Question.Niveau niveau);

    // par categorie et niveau
    List<Question> findByCategorieAndNiveau (Question.Categorie categorie, Question.Niveau niveau);

    // avec un minimum de points
    List<Question> findByPointsGreaterThan (Integer pointmin);

    // questions d'une categorie triée par niveau
    List<Question> findByCategorieOrderByNiveauAsc (Question.Categorie categorie);

    // trier par titre
    List<Question> findAllByOrderByTitreAsc();

    // recherche avec un mot du titre
    List<Question> findByTitreContainingIgnoreCase(String mot);

    // nombre de questions par categorie
    long countByCategorie (Question.Categorie categorie);

}

