package com.skillpass.backend;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.entity.Question;
import com.skillpass.backend.entity.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestEntities {

    @Bean
    public CommandLineRunner testerEntites(){
        return args -> {
            System.out.println("Test des entités");

            // test 1 : Question
            System.out.println(" Test création Question...");
            Question q = new Question();
            q.setTitre("Qu'est-ce que Spring Boot?");
            q.setContenu("Framework pour applications Java");
            q.setCategorie(Question.Categorie.SPRING);
            q.setNiveau(Question.Niveau.DÉBUTANT);
            q.setPoints(5);

            System.out.println("✅ Question créée : " + q.getTitre());
            System.out.println("   Catégorie : " + q.getCategorie());
            System.out.println("   Niveau : " + q.getNiveau());
            System.out.println("   Points : " + q.getPoints());
            //
            //=============
            // Test 2 : Option
            System.out.println(" Test création Options...");

            Option o1 = new Option();
            o1.setTexte("Un framework Java");
            o1.setCorrecte(true);

            Option o2 = new Option();
            o2.setTexte("un langage de programmation ");
            o2.setCorrecte(false);

            System.out.println("Option 1 : " + o1.getTexte() + " (correcte: " + o1.isCorrecte() + ")");
            System.out.println("Option 2 : " + o2.getTexte() + " (correcte: " + o2.isCorrecte() + ")");
            //=================
            // Test 3 : Relation Question - Option
            q.addOption(o1);
            q.addOption(o2);
            System.out.println(" Question a " + q.getOptions().size() + " options");

            System.out.println(" Option 1 appartient à : " + (o1.getQuestion() != null ? "une question" : "RIEN"));
            System.out.println("Titre de la question de Option 1 : " + o1.getQuestion().getTitre());


            //======================
            //Test 4 : Création d'un Test

            System.out.println(" Test création Test...");

            Test test = new Test();
            test.setTitre("Test Spring Boot");
            test.setDescription("Questions de base sur Spring Boot");
            test.setDureeMinutes(20);
            test.addQuestion(q);

            System.out.println(" Test créé : " + test.getTitre());
            System.out.println("   Durée : " + test.getDureeMinutes() + " min");
            System.out.println("   Questions : " + test.getNombreQuestions());
            System.out.println("   Durée estimée : " + test.getDureeEstimeeMinutes() + " min");

            System.out.println(" Question est dans " + q.getTests().size() + " test(s)");


            // ============================
            // Test5 : MÉTHODES UTILITAIRES

            System.out.println(" Test méthodes utilitaires");
            Option o3 = new Option();
            o3.marquerCommeCorrecte();
            System.out.println("Option marquée comme correcte : " + o3.isCorrecte());


            Question q2 = new Question();
            Option opt = q2.createOption("Nouvelle option", true);
            System.out.println(" Option créée via createOption : " + opt.getTexte());
            System.out.println("   Correcte : " + opt.isCorrecte());

            // ==============================
            // TEST 6 : VÉRIFICATION DES ENUMS

            System.out.println("Test des énumérations...");

            System.out.println("Catégories disponibles : ");
            for (Question.Categorie cat : Question.Categorie.values()) {
                System.out.println("   - " + cat);
            }

            System.out.println("Niveaux disponibles : ");
            for (Question.Niveau niv : Question.Niveau.values()) {
                System.out.println("   - " + niv);
            }

            System.out.println("==============================");
            System.out.println("\n🎉 TESTS DES ENTITÉS RÉUSSIS !");
            System.out.println("==============================");


        };



    }
}
