package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.entity.Question;
import com.skillpass.backend.entity.Test;
import com.skillpass.backend.entity.User;
import com.skillpass.backend.repository.QuestionRepository;
import com.skillpass.backend.repository.TestRepository;
import com.skillpass.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ActiveProfiles("test")
@DisplayName("TestController Tests")
class TestControllerTest {

    @LocalServerPort
    private int port;

    private WebClient webClient;
    private WebClient adminWebClient;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Test test;
    private Question question;
    private String userToken;
    private String adminToken;

    @BeforeEach
    void setUp() {
        testRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();

        // Crée admin et user
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setNom("Admin");
        admin.setPrenom("Super");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        User user = new User();
        user.setEmail("user@gmail.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setNom("User");
        user.setPrenom("Normal");
        user.setRole(User.Role.USER);
        userRepository.save(user);

        // Login
        WebClient loginClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();

        Map adminLogin = new java.util.LinkedHashMap<>();
        adminLogin.put("email", "admin@gmail.com");
        adminLogin.put("password", "password123");
        Map adminResponse = loginClient.post().uri("/api/auth/login")
                .bodyValue(adminLogin).retrieve().bodyToMono(Map.class).block();
        adminToken = (String) adminResponse.get("token");

        Map userLogin = new java.util.LinkedHashMap<>();
        userLogin.put("email", "user@gmail.com");
        userLogin.put("password", "password123");
        Map userResponse = loginClient.post().uri("/api/auth/login")
                .bodyValue(userLogin).retrieve().bodyToMono(Map.class).block();
        userToken = (String) userResponse.get("token");

        // Crée une question
        Option opt1 = new Option();
        opt1.setTexte("Un framework Java");
        opt1.setCorrecte(true);

        Option opt2 = new Option();
        opt2.setTexte("Un langage");
        opt2.setCorrecte(false);

        question = new Question();
        question.setTitre("Qu'est-ce que Spring Boot ?");
        question.setCategorie(Question.Categorie.SPRING);
        question.setNiveau(Question.Niveau.DÉBUTANT);
        question.setPoints(5);
        question.setOptions(new ArrayList<>(Arrays.asList(opt1, opt2)));
        opt1.setQuestion(question);
        opt2.setQuestion(question);
        questionRepository.save(question);

        // Crée un test
        test = new Test();
        test.setTitre("Test Spring Boot");
        test.setDescription("Test de Spring");
        test.setDureeMinutes(30);
        test.setQuestions(new ArrayList<>(List.of(question)));
        testRepository.save(test);

        // WebClients
        webClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + userToken)
                .build();

        adminWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + adminToken)
                .build();
    }

    @org.junit.jupiter.api.Test
    @DisplayName("GET /api/tests - Retourne tous les tests")
    void shouldGetAllTests() {
        List response = webClient.get()
                .uri("/api/tests")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(1);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("GET /api/tests/{id} - Retourne un test par id")
    void shouldGetTestById() {
        Map response = webClient.get()
                .uri("/api/tests/" + test.getId())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.get("titre")).isEqualTo("Test Spring Boot");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("GET /api/tests/{id}/questions - Retourne les questions sans réponses")
    void shouldGetTestQuestions() {
        List response = webClient.get()
                .uri("/api/tests/" + test.getId() + "/questions")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(1);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("POST /api/tests/random - Admin peut créer un test aléatoire")
    void shouldCreateRandomTestAsAdmin() {
        Map response = adminWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/tests/random")
                        .queryParam("titre", "Nouveau test")
                        .queryParam("description", "Description")
                        .queryParam("categorie", "SPRING")
                        .queryParam("questionCount", 1)
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.get("titre")).isEqualTo("Nouveau test");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("POST /api/tests/random - User ne peut pas créer un test")
    void shouldNotCreateTestAsUser() {
        Map<String, Object> body = new java.util.LinkedHashMap<>();
        body.put("titre", "Test");
        body.put("categorie", "SPRING");
        body.put("questionCount", 1);

        assertThatThrownBy(() ->
                webClient.post()
                        .uri("/api/tests/random")
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).isInstanceOf(WebClientResponseException.class)
                .hasMessageContaining("403");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("POST /api/tests/{id}/calculate-score - Calcule le score")
    void shouldCalculateScore() {
        Map response = adminWebClient.post()
                .uri("/api/tests/" + test.getId() + "/calculate-score")
                .bodyValue(new ArrayList<Long>())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response).containsKey("score");
        assertThat(response).containsKey("pourcentage");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("DELETE /api/tests/{id} - Admin peut supprimer un test")
    void shouldDeleteTestAsAdmin() {
        Test testToDelete = new Test();
        testToDelete.setTitre("Test à supprimer");
        testToDelete.setDureeMinutes(10);
        testToDelete.setQuestions(new ArrayList<>());
        testRepository.save(testToDelete);

        adminWebClient.delete()
                .uri("/api/tests/" + testToDelete.getId())
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        assertThat(testRepository.findById(testToDelete.getId())).isEmpty();
    }
}