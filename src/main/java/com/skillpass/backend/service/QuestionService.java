package com.skillpass.backend.service;

import com.skillpass.backend.entity.Question;
import com.skillpass.backend.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // Methode questions aleatoires

    public List<Question> getRandomQuestions (int count){
        List<Question> questions = questionRepository.findAll();

        Collections.shuffle(questions);

        List<Question> randomQuestions = questions.stream()
                .limit(count)
                .collect(Collectors.toList());

        // melange des options :
      randomQuestions.forEach(randomQuestion -> {
          if (randomQuestion.getOptions() != null){
              Collections.shuffle(randomQuestion.getOptions());
          }
      });

        return randomQuestions;
    }

    // methode pour trier les questions par categorie
     public List<Question> getQuestionsByCategory(Question.Categorie categorie, int count){
        List<Question> categoryQuestions = questionRepository.findByCategorie(categorie);

        Collections.shuffle(categoryQuestions);
        return categoryQuestions.stream()
                .limit(count)
                .collect(Collectors.toList());

     }

     // methode  pour trier par niveau
    public List<Question> getQuestionsByNiveau(Question.Niveau niveau, int count){
        List<Question> niveauQuestions = questionRepository.findByNiveau(niveau);

        Collections.shuffle(niveauQuestions);
        return niveauQuestions.stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    //methode pour creer une question

    public Question createQuestion(Question question){

        //validations

        if (question.getTitre() == null || question.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre de la question est requis");
        }

        if (question.getOptions() == null || question.getOptions().isEmpty()) {
            throw new IllegalArgumentException("Une question doit avoir au moins une option");
        }

        boolean hasCorrectAnswer = question.getOptions().stream()
                .anyMatch(option -> option.isCorrecte());

        if (!hasCorrectAnswer) {
            throw new IllegalArgumentException("Une question doit avoir au moins une bonne réponse");
        }

        return questionRepository.save(question);

    }

    // modifier une question
    public Question updateQuestion(Long id, Question updatedQuestion) {

        Question existing = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question non trouvée: " + id));

        if (updatedQuestion.getTitre() != null) {
            existing.setTitre(updatedQuestion.getTitre());
        }

        if (updatedQuestion.getContenu() != null) {
            existing.setContenu(updatedQuestion.getContenu());
        }

        if (updatedQuestion.getCategorie() != null) {
            existing.setCategorie(updatedQuestion.getCategorie());
        }

        if (updatedQuestion.getNiveau() != null) {
            existing.setNiveau(updatedQuestion.getNiveau());
        }

        if (updatedQuestion.getPoints() != null && updatedQuestion.getPoints() > 0) {
            existing.setPoints(updatedQuestion.getPoints());
        }

        return questionRepository.save(existing);
    }

    //supprimer une question
    public void deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question non trouvée: " + id));

        if (question.getTests() != null && !question.getTests().isEmpty()) {
            throw new IllegalStateException(
                    "Impossible de supprimer la question ID " + id +
                            ". Elle est utilisée dans " + question.getTests().size() + " test(s)."
            );
        }
        questionRepository.deleteById(id);
    }

    // lister toutes les questions
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    //trouver une question par id
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question non trouvée: " + id));
    }



    // Statistiques (KPIs)
    public String getStatistics() {
        long totalQuestions = questionRepository.count();
        long springQuestions = questionRepository.countByCategorie(Question.Categorie.SPRING);
        long javaQuestions = questionRepository.countByCategorie(Question.Categorie.JAVA);

        return String.format(
                "📊 Statistiques : %d questions total\n" +
                        "   - Spring: %d questions\n" +
                        "   - Java: %d questions",
                totalQuestions, springQuestions, javaQuestions
        );
    }


}
