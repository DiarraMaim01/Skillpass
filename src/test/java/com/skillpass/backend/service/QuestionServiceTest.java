package com.skillpass.backend.service;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.entity.Question;
import com.skillpass.backend.entity.Test;
import com.skillpass.backend.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("QuestionService Tests")
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question question;
    private Option optionCorrecte;
    private Option optionFausse;

    @BeforeEach
    void setUp() {
        // Question de base réutilisée dans tous les tests
        question = new Question();
        question.setId(1L);
        question.setTitre("Qu'est-ce que Spring Boot ?");
        question.setContenu("Définissez Spring Boot");
        question.setCategorie(Question.Categorie.SPRING);
        question.setNiveau(Question.Niveau.DÉBUTANT);
        question.setPoints(5);

        optionCorrecte = new Option();
        optionCorrecte.setId(1L);
        optionCorrecte.setTexte("Un framework Java");
        optionCorrecte.setCorrecte(true);
        optionCorrecte.setQuestion(question);

        optionFausse = new Option();
        optionFausse.setId(2L);
        optionFausse.setTexte("Un langage de programmation");
        optionFausse.setCorrecte(false);
        optionFausse.setQuestion(question);

        question.setOptions(new ArrayList<>(Arrays.asList(optionCorrecte, optionFausse)));
    }

    @Nested
    @DisplayName("getAllQuestions()")
    class GetAllQuestions {

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne toutes les questions")
        void shouldReturnAllQuestions() {
            when(questionRepository.findAll()).thenReturn(Arrays.asList(question));

            List<Question> result = questionService.getAllQuestions();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTitre()).isEqualTo("Qu'est-ce que Spring Boot ?");
            verify(questionRepository, times(1)).findAll();
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne une liste vide si aucune question")
        void shouldReturnEmptyList() {
            when(questionRepository.findAll()).thenReturn(new ArrayList<>());

            List<Question> result = questionService.getAllQuestions();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("getQuestionById()")
    class GetQuestionById {

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne la question si elle existe")
        void shouldReturnQuestionWhenExists() {
            when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

            Question result = questionService.getQuestionById(1L);

            // THEN
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getTitre()).isEqualTo("Qu'est-ce que Spring Boot ?");
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si la question n'existe pas")
        void shouldThrowExceptionWhenNotFound() {
            when(questionRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> questionService.getQuestionById(99L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Question non trouvée");
        }
    }

    @Nested
    @DisplayName("createQuestion()")
    class CreateQuestion {

        @org.junit.jupiter.api.Test
        @DisplayName("Crée une question valide avec succès")
        void shouldCreateValidQuestion() {
            when(questionRepository.save(any(Question.class))).thenReturn(question);

            Question result = questionService.createQuestion(question);

            assertThat(result).isNotNull();
            assertThat(result.getTitre()).isEqualTo("Qu'est-ce que Spring Boot ?");
            verify(questionRepository, times(1)).save(question);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si le titre est vide")
        void shouldThrowExceptionWhenTitreIsEmpty() {
            question.setTitre("");

            assertThatThrownBy(() -> questionService.createQuestion(question))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("titre");
            verify(questionRepository, never()).save(any());
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si aucune option")
        void shouldThrowExceptionWhenNoOptions() {
            question.setOptions(new ArrayList<>());

            assertThatThrownBy(() -> questionService.createQuestion(question))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("option");
            verify(questionRepository, never()).save(any());
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si aucune bonne réponse")
        void shouldThrowExceptionWhenNoCorrectAnswer() {
            optionCorrecte.setCorrecte(false); // plus de bonne réponse

            assertThatThrownBy(() -> questionService.createQuestion(question))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("bonne réponse");
            verify(questionRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("updateQuestion()")
    class UpdateQuestion {

        @org.junit.jupiter.api.Test
        @DisplayName("Met à jour une question existante")
        void shouldUpdateExistingQuestion() {
            Question updated = new Question();
            updated.setTitre("Nouveau titre");
            updated.setPoints(10);

            when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
            when(questionRepository.save(any(Question.class))).thenReturn(question);

            Question result = questionService.updateQuestion(1L, updated);

            verify(questionRepository, times(1)).save(any());
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si la question n'existe pas")
        void shouldThrowExceptionWhenNotFound() {
            // GIVEN
            when(questionRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> questionService.updateQuestion(99L, question))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("deleteQuestion()")
    class DeleteQuestion {

        @org.junit.jupiter.api.Test
        @DisplayName("Supprime une question sans tests associés")
        void shouldDeleteQuestionWithoutTests() {
            question.setTests(new ArrayList<>());
            when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

            questionService.deleteQuestion(1L);

            verify(questionRepository, times(1)).deleteById(1L);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si la question est utilisée dans un test")
        void shouldThrowExceptionWhenUsedInTest() {
            Test test = new Test();
            test.setId(1L);
            question.setTests(new ArrayList<>(List.of(test)));
            when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

            assertThatThrownBy(() -> questionService.deleteQuestion(1L))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("test");
            verify(questionRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("getRandomQuestions()")
    class GetRandomQuestions {

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne le bon nombre de questions aléatoires")
        void shouldReturnCorrectCount() {
            Question q2 = new Question();
            q2.setId(2L);
            q2.setOptions(new ArrayList<>());

            when(questionRepository.findAll()).thenReturn(Arrays.asList(question, q2));

            List<Question> result = questionService.getRandomQuestions(1);

            assertThat(result).hasSize(1);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne toutes les questions si count > total")
        void shouldReturnAllWhenCountExceedsTotal() {
            when(questionRepository.findAll()).thenReturn(Arrays.asList(question));

            List<Question> result = questionService.getRandomQuestions(10);

            assertThat(result).hasSize(1);
        }
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Retourne les questions par niveau")
    void shouldGetQuestionsByNiveau() {
        // GIVEN
        when(questionRepository.findByNiveau(Question.Niveau.DÉBUTANT))
                .thenReturn(List.of(question));

        // WHEN
        List<Question> result = questionService.getQuestionsByNiveau(Question.Niveau.DÉBUTANT, 10);

        // THEN
        assertThat(result).hasSize(1);
        verify(questionRepository).findByNiveau(Question.Niveau.DÉBUTANT);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Retourne les questions par catégorie")
    void shouldGetQuestionsByCategorie() {
        // GIVEN
        when(questionRepository.findByCategorie(Question.Categorie.SPRING))
                .thenReturn(List.of(question));

        // WHEN
        List<Question> result = questionService.getQuestionsByCategory(Question.Categorie.SPRING, 10);

        // THEN
        assertThat(result).hasSize(1);
        verify(questionRepository).findByCategorie(Question.Categorie.SPRING);
    }
}