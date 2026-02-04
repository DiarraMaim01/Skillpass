package com.skillpass.backend.service;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.entity.Question;
import com.skillpass.backend.entity.Test;
import com.skillpass.backend.repository.QuestionRepository;
import com.skillpass.backend.repository.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class TestService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    //lister tous les test
    public List<Test> getTests() {
        return testRepository.findAll();
    }

    //creer un test aleatoire
    public Test createRandomTest(String titre, String description,Question.Categorie categorie, int questionCount) {

        Test test = new Test();
        test.setTitre(titre);
        test.setDescription(description);

        List<Question> questions = questionRepository.findByCategorie(categorie);
        Collections.shuffle(questions);

        questions.stream()
                .limit(questionCount)
                .forEach(test::addQuestion);

        test.setDureeMinutes(test.getNombreQuestions() * 2);

        return testRepository.save(test);

    }

    //calcul Score

    public int calculScoreTest (Test test, List<Long> selectedOptionId){
          int score = 0;

          for (Question question : test.getQuestions()) {
              boolean hasCorrectOption = question.getOptions().stream()
                      .filter(Option::isCorrecte)
                      .anyMatch(option -> selectedOptionId.contains(option.getId()));
              if (hasCorrectOption){
                  score += question.getPoints();
              }


          }
        return score;
    }

    // Resultat du test
    public String getTestResult(Test test, int score) {
        int totalQuestions = test.getNombreQuestions();
        int totalPoints = test.getQuestions().stream()
                .mapToInt(Question::getPoints)
                .sum();

        double percentage = (score * 100.0) / totalPoints;

        return String.format(
                "🎯 Résultat du test : %s\n" +
                        "📊 Score : %d/%d points (%.1f%%)\n" +
                        "❓ Questions : %d\n" + "⏱️ Durée estimée : %d min",
                test.getTitre(), score, totalPoints, percentage,
                totalQuestions, test.getDureeMinutes()
        );
    }


// methode tri

    public List<Test>  findTestByTitre (String titre) {
         return testRepository.findByTitreIgnoreCase(titre);
    }




}
