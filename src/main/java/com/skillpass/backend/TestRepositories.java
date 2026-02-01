package com.skillpass.backend;

import com.skillpass.backend.entity.*;
import com.skillpass.backend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class TestRepositories {

    @Bean
    public CommandLineRunner testerRepositories(
            QuestionRepository questionRepository,
            OptionRepository optionRepository,
            TestRepository testRepository) {

        return args -> {
            System.out.println(" TEST DES REPOSITORIES JPA");

            // Test 1 : creer et sauvegarder une question

            System.out.println("Création d'une question...");

            Question q = new Question();
            q.setTitre("Spring Boot Basics");
            q.setContenu("Quelle annotation démarre une app Spring Boot?");
            q.setCategorie(Question.Categorie.SPRING);
            q.setNiveau(Question.Niveau.DÉBUTANT);
            q.setPoints(5);

            q.createOption("@SpringBootApplication", true);
            q.createOption("@SpringApplication", false);
            q.createOption("@Boot", false);

            Question savedQuestion = questionRepository.save(q);
            System.out.println(" Question sauvegardée avec ID: " + savedQuestion.getId());

            // test 2 : compter et lister

            System.out.println(" Comptage et listing...");

            long totalQuestions = questionRepository.count();
            System.out.println(" Total questions en base: " + totalQuestions);

            long questionsSpring = questionRepository.countByCategorie(Question.Categorie.SPRING);
            System.out.println(" Questions Spring: " + questionsSpring);


            // TEST 3 : recherche

            System.out.println(" Recherche...");

            var questionsDebutant = questionRepository.findByNiveau(Question.Niveau.DÉBUTANT);
            System.out.println(" Questions débutant: " + questionsDebutant.size());

            var questionsSpringDebutant = questionRepository.findByCategorieAndNiveau(
                    Question.Categorie.SPRING,
                    Question.Niveau.DÉBUTANT
            );
            System.out.println(" Questions Spring + débutant: " + questionsSpringDebutant.size());


            // TEST 4 : Options

            System.out.println(" Test des options...");

            var options = optionRepository.findByQuestionId(savedQuestion.getId());
            System.out.println("Options de la question: " + options.size());

            var bonnesReponses = optionRepository.findByQuestionIdAndCorrecteTrue(savedQuestion.getId());
            System.out.println(" Bonnes réponses: " + bonnesReponses.size());

            // TEST 5 : creer un test

            System.out.println(" Création d'un test...");

            Test test = new Test();
            test.setTitre("Test Spring Boot Débutant");
            test.setDescription("Évalue tes connaissances de base en Spring Boot");
            test.setDureeMinutes(30);

            test.addQuestion(savedQuestion);

            Test savedTest = testRepository.save(test);
            System.out.println("Test sauvegardé avec ID: " + savedTest.getId());
            System.out.println(" Nombre de questions dans le test: " + test.getNombreQuestions());
            System.out.println("Durée estimée: " + test.getDureeEstimeeMinutes() + " min");

            // RÉCAPITULATIF

            System.out.println(" RÉCAPITULATIF DES TESTS");
            System.out.println("==========================");
            System.out.println(" REPOSITORIES TESTÉS AVEC SUCCÈS !");
        };
    }
}