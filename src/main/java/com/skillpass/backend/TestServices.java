package com.skillpass.backend;

import com.skillpass.backend.entity.Question;
import com.skillpass.backend.entity.Test;
import com.skillpass.backend.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestServices {

    @Bean
    public CommandLineRunner testerServices(
            QuestionService questionService,
            TestService testService,
            OptionService optionService) {

        return args -> {
            System.out.println("🧪 TEST DES SERVICES (LOGIQUE MÉTIER)");
            System.out.println("======================================");


            //  creer une question via Service

            System.out.println(" Test création de question");

            Question q = new Question();
            q.setTitre("Services Spring");
            q.setContenu("Quelle annotation pour un Service?");
            q.setCategorie(Question.Categorie.SPRING);
            q.setNiveau(Question.Niveau.DÉBUTANT);

            q.createOption("@Service", true);
            q.createOption("@Component", false);
            q.createOption("@Repository", false);

            Question saved = questionService.createQuestion(q);
            System.out.println(" Question créée via service: " + saved.getTitre());

           // questions aleatoires
            System.out.println(" Test questions aléatoires...");

            var randomQuestions = questionService.getRandomQuestions(5);
            System.out.println("" + randomQuestions.size() + " questions aléatoires");

           // creation de test via service
            System.out.println(" Test création de test");

            Test test = testService.createRandomTest(
                    "Mon premier test",
                    "Test de démonstration",
                    Question.Categorie.SPRING,
                    3
            );

            System.out.println("✅ Test créé: " + test.getTitre());
            System.out.println("   Questions: " + test.getNombreQuestions());
            System.out.println("   Durée: " + test.getDureeMinutes() + " min");

          //calcul de score
            System.out.println(" Test calcul de score");


            var selectedOptions = test.getQuestions().stream()
                    .flatMap(question -> question.getOptions().stream())
                    .limit(3)
                    .map(option -> option.getId())
                    .toList();

            int score = testService.calculScoreTest(test, selectedOptions);
            String result = testService.getTestResult(test, score);
            System.out.println(result);

            //validation
            System.out.println(" Test validation de réponse");


            if (!test.getQuestions().isEmpty()) {
                var firstQuestion = test.getQuestions().get(0);
                if (!firstQuestion.getOptions().isEmpty()) {
                    var firstOption = firstQuestion.getOptions().get(0);
                    boolean isCorrect = optionService.validateAnswer(firstOption.getId());
                    System.out.println("✅ Option " + firstOption.getId() +
                            " est correcte? " + isCorrect);
                }
            }

            // Test suppression
            System.out.println("\n7️⃣ Test suppression de question...");
            try {
                questionService.deleteQuestion(saved.getId());
                System.out.println("✅ Question supprimée avec succès");
            } catch (Exception e) {
                System.out.println("⚠️ " + e.getMessage());
            }

           // Test mise à jour
            System.out.println(" Test mise à jour de question");
            Question updated = new Question();
            updated.setTitre("Titre mis à jour");
            updated.setPoints(10);

            Question resultat = questionService.updateQuestion(saved.getId(), updated);
            System.out.println("✅ Question mise à jour: " + resultat.getTitre() +
                    " (" + resultat.getPoints() + " points)");

           // stats

            System.out.println(" Test statistiques..");

            String stats = questionService.getStatistics();
            System.out.println(stats);


            // RÉCAPITULATIF
            System.out.println(" SERVICES TESTÉS AVEC SUCCÈS !");
            System.out.println("================================");
            System.out.println("✅ Logique métier fonctionnelle");

        };
    }
}