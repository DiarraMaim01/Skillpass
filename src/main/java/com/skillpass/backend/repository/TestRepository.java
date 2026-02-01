package com.skillpass.backend.repository;

import com.skillpass.backend.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository  extends JpaRepository<Test,Long> {

    // trouver un test par son titre
    List<Test> findByTitreIgnoreCase(String texte);

    //recherche
    List<Test> findByTitreContainingIgnoreCase(String mot);

    // trouver par durée
    List<Test> findByDureeMinutesLessThanEqual( Integer dureeMax);
    List<Test> findByDureeMinutesGreaterThanEqual( Integer dureeMin);

    //trier par titre
    List<Test> findAllByOrderByTitreAsc();

    // trier par durée du plus court au plus long
    List<Test> findAllByOrderByDureeMinutesAsc();


}
