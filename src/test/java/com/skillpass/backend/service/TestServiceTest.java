package com.skillpass.backend.service;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.entity.Question;
import com.skillpass.backend.entity.Test;
import com.skillpass.backend.repository.QuestionRepository;
import com.skillpass.backend.repository.TestRepository;
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
@DisplayName("TestService Tests")
class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private TestService testService;

    private Test test;
    private Question question;
    private Option optionCorrecte;
    private Option optionFausse;

    @BeforeEach
    void setUp() {
        optionCorrecte = new Option();
        optionCorrecte.setId(1L);
        optionCorrecte.setTexte("Un framework Java");
        optionCorrecte.setCorrecte(true);

        optionFausse = new Option();
        optionFausse.setId(2L);
        optionFausse.setTexte("Un langage");
        optionFausse.setCorrecte(false);

        question = new Question();
        question.setId(1L);
        question.setTitre("Qu'est-ce que Spring Boot ?");
        question.setPoints(5);
        question.setCategorie(Question.Categorie.SPRING);
        question.setOptions(new ArrayList<>(Arrays.asList(optionCorrecte, optionFausse)));

        test = new Test();
        test.setId(1L);
        test.setTitre("Test Spring");
        test.setDescription("Test de Spring Boot");
        test.setDureeMinutes(30);
        test.setQuestions(new ArrayList<>(List.of(question)));
    }

    @Nested
    @DisplayName("getTests()")
    class GetTests {

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne tous les tests")
        void shouldReturnAllTests() {

            when(testRepository.findAll()).thenReturn(Arrays.asList(test));

            List<Test> result = testService.getTests();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTitre()).isEqualTo("Test Spring");
            verify(testRepository, times(1)).findAll();
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne une liste vide si aucun test")
        void shouldReturnEmptyList() {
            when(testRepository.findAll()).thenReturn(new ArrayList<>());

            List<Test> result = testService.getTests();

            assertThat(result).isEmpty();
            verify(testRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("getTestById()")
    class GetTestById {

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne le test si il existe")
        void shouldReturnTestWhenExists() {
            when(testRepository.findById(1L)).thenReturn(Optional.of(test));

            Test result = testService.getTestById(1L);

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getTitre()).isEqualTo("Test Spring");
            verify(testRepository, times(1)).findById(1L);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si le test n'existe pas")
        void shouldThrowExceptionWhenNotFound() {
            when(testRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> testService.getTestById(99L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Test non trouvé");
            verify(testRepository, times(1)).findById(99L);
        }
    }

    @Nested
    @DisplayName("createRandomTest()")
    class CreateRandomTest {

        @org.junit.jupiter.api.Test
        @DisplayName("Crée un test aléatoire avec les bonnes questions")
        void shouldCreateRandomTest() {
            when(questionRepository.findByCategorie(Question.Categorie.SPRING))
                    .thenReturn(Arrays.asList(question));
            when(testRepository.save(any(Test.class))).thenReturn(test);

            Test result = testService.createRandomTest(
                    "Test Spring",
                    "Description",
                    Question.Categorie.SPRING,
                    1
            );

            assertThat(result).isNotNull();
            assertThat(result.getTitre()).isEqualTo("Test Spring");
            verify(questionRepository, times(1)).findByCategorie(Question.Categorie.SPRING);
            verify(testRepository, times(1)).save(any(Test.class));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Limite le nombre de questions si count > disponibles")
        void shouldLimitQuestionsWhenCountExceedsAvailable() {
            when(questionRepository.findByCategorie(Question.Categorie.SPRING))
                    .thenReturn(Arrays.asList(question));
            when(testRepository.save(any(Test.class))).thenAnswer(i -> i.getArgument(0));

            Test result = testService.createRandomTest(
                    "Test Spring",
                    "Description",
                    Question.Categorie.SPRING,
                    10
            );

            assertThat(result.getNombreQuestions()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("updateTest()")
    class UpdateTest {

        @org.junit.jupiter.api.Test
        @DisplayName("Met à jour un test existant")
        void shouldUpdateExistingTest() {
            Test updated = new Test();
            updated.setTitre("Nouveau titre");
            updated.setDescription("Nouvelle description");
            updated.setDureeMinutes(60);

            when(testRepository.findById(1L)).thenReturn(Optional.of(test));
            when(testRepository.save(any(Test.class))).thenReturn(test);

            Test result = testService.updateTest(1L, updated);

            assertThat(result).isNotNull();
            verify(testRepository, times(1)).findById(1L);
            verify(testRepository, times(1)).save(any(Test.class));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si le test n'existe pas")
        void shouldThrowExceptionWhenNotFound() {
            when(testRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> testService.updateTest(99L, test))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Test non trouvé");
        }
    }

    @Nested
    @DisplayName("deleteTest()")
    class DeleteTest {

        @org.junit.jupiter.api.Test
        @DisplayName("Supprime un test existant")
        void shouldDeleteExistingTest() {
            when(testRepository.existsById(1L)).thenReturn(true);

            testService.deleteTest(1L);

            verify(testRepository, times(1)).deleteById(1L);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si le test n'existe pas")
        void shouldThrowExceptionWhenNotFound() {
            when(testRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> testService.deleteTest(99L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Test non trouvé");
            verify(testRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("addQuestionToTest()")
    class AddQuestionToTest {

        @org.junit.jupiter.api.Test
        @DisplayName("Ajoute une question à un test")
        void shouldAddQuestionToTest() {
            Question newQuestion = new Question();
            newQuestion.setId(2L);
            newQuestion.setTitre("Nouvelle question");

            when(testRepository.findById(1L)).thenReturn(Optional.of(test));
            when(questionRepository.findById(2L)).thenReturn(Optional.of(newQuestion));
            when(testRepository.save(any(Test.class))).thenReturn(test);

            Test result = testService.addQuestionToTest(1L, 2L);

            assertThat(result).isNotNull();
            verify(testRepository, times(1)).save(any(Test.class));
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si le test n'existe pas")
        void shouldThrowExceptionWhenTestNotFound() {
            when(testRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> testService.addQuestionToTest(99L, 1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Test non trouvé");
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Lève une exception si la question n'existe pas")
        void shouldThrowExceptionWhenQuestionNotFound() {
            when(testRepository.findById(1L)).thenReturn(Optional.of(test));
            when(questionRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> testService.addQuestionToTest(1L, 99L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Question non trouvée");
        }
    }

    @Nested
    @DisplayName("calculScoreTest()")
    class CalculScoreTest {

        @org.junit.jupiter.api.Test
        @DisplayName("Calcule le score avec une bonne réponse")
        void shouldCalculateScoreWithCorrectAnswer() {
            List<Long> selectedOptionIds = Arrays.asList(1L);

            int score = testService.calculScoreTest(test, selectedOptionIds);

            assertThat(score).isEqualTo(5);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne 0 si mauvaise réponse")
        void shouldReturnZeroWithWrongAnswer() {
            List<Long> selectedOptionIds = Arrays.asList(2L);

            int score = testService.calculScoreTest(test, selectedOptionIds);

            assertThat(score).isEqualTo(0);
        }

        @org.junit.jupiter.api.Test
        @DisplayName("Retourne 0 si aucune réponse sélectionnée")
        void shouldReturnZeroWithNoAnswers() {
            List<Long> selectedOptionIds = new ArrayList<>();

            int score = testService.calculScoreTest(test, selectedOptionIds);

            assertThat(score).isEqualTo(0);
        }
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Recherche un test par titre")
    void shouldFindTestByTitre() {
        // GIVEN
        when(testRepository.findByTitreIgnoreCase("Spring Boot"))
                .thenReturn(List.of(test));

        // WHEN
        List<com.skillpass.backend.entity.Test> result = testService.findTestByTitre("Spring Boot");

        // THEN
        assertThat(result).hasSize(1);
        verify(testRepository).findByTitreIgnoreCase("Spring Boot");
    }


    @org.junit.jupiter.api.Test
    @DisplayName("Retire une question d'un test")
    void shouldRemoveQuestionFromTest() {
        // GIVEN
        test.getQuestions().add(question);
        when(testRepository.findById(1L)).thenReturn(Optional.of(test));
        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        when(testRepository.save(any())).thenAnswer(invocation -> {
            test.getQuestions().remove(question);
            return test;
        });

        // WHEN
        com.skillpass.backend.entity.Test result = testService.removeQuestionFromTest(1L, 1L);

        // THEN
        assertThat(result.getQuestions()).doesNotContain(question);
        verify(testRepository).save(test);
    }
}