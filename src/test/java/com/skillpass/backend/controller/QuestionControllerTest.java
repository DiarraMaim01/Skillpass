package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.entity.Question;
import com.skillpass.backend.repository.TestRepository;
import com.skillpass.backend.repository.UserRepository;
import com.skillpass.backend.entity.User;
import com.skillpass.backend.security.JwtService;
import com.skillpass.backend.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ActiveProfiles("test")
@DisplayName("QuestionController Tests")
class QuestionControllerTest {

    @LocalServerPort
    private int port;

    private WebClient webClient;
    private WebClient adminWebClient;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private String adminToken;
    private String userToken;
    private Question question;


    @BeforeEach
    void setUp() {
        testRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();

        // Crée admin et user en base
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

        // Login pour obtenir les vrais tokens
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
        Option optionCorrecte = new Option();
        optionCorrecte.setTexte("Un framework Java");
        optionCorrecte.setCorrecte(true);

        Option optionFausse = new Option();
        optionFausse.setTexte("Un langage");
        optionFausse.setCorrecte(false);

        question = new Question();
        question.setTitre("Qu'est-ce que Spring Boot ?");
        question.setContenu("Définissez Spring Boot");
        question.setCategorie(Question.Categorie.SPRING);
        question.setNiveau(Question.Niveau.DÉBUTANT);
        question.setPoints(5);
        question.setOptions(new ArrayList<>(Arrays.asList(optionCorrecte, optionFausse)));
        optionCorrecte.setQuestion(question);
        optionFausse.setQuestion(question);
        questionRepository.save(question);

        // WebClients avec vrais tokens
        webClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + userToken)
                .build();

        adminWebClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Authorization", "Bearer " + adminToken)
                .build();
    }

    @Test
    @DisplayName("GET /api/questions - Retourne toutes les questions")
    void shouldGetAllQuestions() {
        List response = adminWebClient.get()  // ← adminWebClient
                .uri("/api/questions")
                .retrieve()
                .bodyToMono(List.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response).hasSize(1);
    }

    @Test
    @DisplayName("GET /api/questions/{id} - Retourne une question par id")
    void shouldGetQuestionById() {
        Map response = adminWebClient.get()  // ← adminWebClient
                .uri("/api/questions/" + question.getId())
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.get("titre")).isEqualTo("Qu'est-ce que Spring Boot ?");
    }


    @Test
    @DisplayName("GET /api/questions/{id} - Retourne 404 si question inexistante")
    void shouldReturn404WhenQuestionNotFound() {
        assertThatThrownBy(() ->
                webClient.get()
                        .uri("/api/questions/9999")
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).isInstanceOf(WebClientResponseException.class);
    }
    @Test
    @DisplayName("POST /api/questions - Admin peut créer une question")
    void shouldCreateQuestionAsAdmin() {
        Map<String, Object> newQuestion = new java.util.LinkedHashMap<>();
        newQuestion.put("titre", "Qu'est-ce que JPA ?");
        newQuestion.put("contenu", "Définissez JPA");
        newQuestion.put("categorie", "JAVA");
        newQuestion.put("niveau", "DÉBUTANT");
        newQuestion.put("points", 5);

        Map opt1 = new java.util.LinkedHashMap<>();
        opt1.put("texte", "Une API Java");
        opt1.put("correcte", true);

        Map opt2 = new java.util.LinkedHashMap<>();
        opt2.put("texte", "Un framework");
        opt2.put("correcte", false);

        newQuestion.put("options", Arrays.asList(opt1, opt2));

        Map response = adminWebClient.post()
                .uri("/api/questions")
                .bodyValue(newQuestion)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        assertThat(response).isNotNull();
        assertThat(response.get("titre")).isEqualTo("Qu'est-ce que JPA ?");
    }

    @Test
    @DisplayName("POST /api/questions - User ne peut pas créer une question")
    void shouldNotCreateQuestionAsUser() {
        Map<String, Object> newQuestion = new java.util.LinkedHashMap<>();
        newQuestion.put("titre", "Test");
        newQuestion.put("options", new ArrayList<>());

        assertThatThrownBy(() ->
                webClient.post()
                        .uri("/api/questions")
                        .bodyValue(newQuestion)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .block()
        ).isInstanceOf(WebClientResponseException.class)
                .hasMessageContaining("403");
    }

    @Test
    @DisplayName("DELETE /api/questions/{id} - Admin peut supprimer une question")
    void shouldDeleteQuestionAsAdmin() {
        // Crée une question sans tests associés
        Option opt = new Option();
        opt.setTexte("Réponse");
        opt.setCorrecte(true);

        Question q = new Question();
        q.setTitre("Question à supprimer");
        q.setCategorie(Question.Categorie.JAVA);
        q.setNiveau(Question.Niveau.DÉBUTANT);
        q.setPoints(5);
        q.setOptions(new ArrayList<>(List.of(opt)));
        opt.setQuestion(q);
        questionRepository.save(q);

        adminWebClient.delete()
                .uri("/api/questions/" + q.getId())
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        assertThat(questionRepository.findById(q.getId())).isEmpty();
    }

    @Test
    @DisplayName("DELETE /api/questions/{id} - User ne peut pas supprimer")
    void shouldNotDeleteQuestionAsUser() {
        assertThatThrownBy(() ->
                webClient.delete()
                        .uri("/api/questions/" + question.getId())
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block()
        ).isInstanceOf(WebClientResponseException.class)
                .hasMessageContaining("403");
    }
}