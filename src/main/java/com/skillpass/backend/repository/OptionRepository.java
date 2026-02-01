package com.skillpass.backend.repository;

import com.skillpass.backend.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    //trouver toutes les options d'une question
    List<Option> findByQuestionId(Long questionId);

    // trouver les bonnes reponses
    List<Option> findByQuestionIdAndCorrecteTrue(Long questionId);

    //trouver les mauvaises reponses
    List<Option> findByQuestionIdAndCorrecteFalse(Long questionId);

    // compter le nombre d'option
    Long countByQuestionId(Long questionId);

    // le nombre de bonne reponse
    Long countByQuestionIdAndCorrecteTrue(Long questionId);

    // vérifier si une option existe pour une question
    boolean existsByQuestionIdAndTexte(Long questionId, String texte);

    //supprimer toutes les options d'une question
    void deleteByQuestionId(Long questionId);


}
