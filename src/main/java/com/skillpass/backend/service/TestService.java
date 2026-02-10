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

    // modifier un test
    public Test updateTest(Long id, Test updatedTest) {
        Test existing = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test non trouvé: " + id));

        if (updatedTest.getTitre() != null) {
            existing.setTitre(updatedTest.getTitre());
        }

        if (updatedTest.getDescription() != null) {
            existing.setDescription(updatedTest.getDescription());
        }

        if (updatedTest.getDureeMinutes() != null && updatedTest.getDureeMinutes() > 0) {
            existing.setDureeMinutes(updatedTest.getDureeMinutes());
        }
        return testRepository.save(existing);
    }

    // supprimer un test
    public void deleteTest(Long id) {

        if (!testRepository.existsById(id)) {
            throw new IllegalArgumentException("Test non trouvé: " + id);
        }

        testRepository.deleteById(id);
    }

   // ajouter une question a un test
    public Test addQuestionToTest(Long testId, Long questionId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test non trouvé: " + testId));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question non trouvée: " + questionId));

        test.addQuestion(question);
        return testRepository.save(test);
    }


    // supprimer une question d'un test
    public Test removeQuestionFromTest(Long testId, Long questionId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test non trouvé: " + testId));

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question non trouvée: " + questionId));

        test.removeQuestion(question);
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
                "Résultat du test : %s\n" +
                        " Score : %d/%d points (%.1f%%)\n" +
                        "❓ Questions : %d\n" + "⏱️ Durée estimée : %d min",
                test.getTitre(), score, totalPoints, percentage,
                totalQuestions, test.getDureeMinutes()
        );
    }



    public Test getTestById(Long id) {
        return testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Test non trouvé: " + id));
    }

// methode tri

    public List<Test>  findTestByTitre (String titre) {
         return testRepository.findByTitreIgnoreCase(titre);
    }




}
