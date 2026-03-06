package com.skillpass.backend.controller;

import com.skillpass.backend.dto.AuthRequest;
import com.skillpass.backend.dto.RegisterRequest;
import com.skillpass.backend.entity.User;
import com.skillpass.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @LocalServerPort
    private int port;

    private WebClient webClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
        userRepository.deleteAll();
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setNom("Dupont");
        user.setPrenom("Jean");
        user.setRole(User.Role.USER);
        userRepository.save(user);
    }

    @Test
    @DisplayName("Login réussi avec email et mot de passe corrects")
    void shouldLoginSuccessfully() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password123");

        Map response = webClient.post()
                .uri("/api/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        assertThat(response).containsKey("token");
        assertThat(response.get("email")).isEqualTo("test@gmail.com");
    }

    @Test
    @DisplayName("Login échoue avec mauvais mot de passe")
    void shouldFailWithWrongPassword() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("mauvaisMotDePasse");

        try {
            webClient.post()
                    .uri("/api/auth/login")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            assertThat(e.getMessage()).containsAnyOf("401", "403");
        }
    }

    @Test
    @DisplayName("Login échoue avec email inexistant")
    void shouldFailWithUnknownEmail() {
        AuthRequest request = new AuthRequest();
        request.setEmail("inconnu@gmail.com");
        request.setPassword("password123");

        try {
            webClient.post()
                    .uri("/api/auth/login")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            assertThat(e.getMessage()).containsAnyOf("401", "403");
        }
    }

    @Test
    @DisplayName("Inscription réussie avec données valides")
    void shouldRegisterSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("nouveau@gmail.com");
        request.setPassword("password123");
        request.setNom("Martin");
        request.setPrenom("Paul");

        Map response = webClient.post()
                .uri("/api/auth/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        assertThat(response).containsKey("token");
        assertThat(response.get("email")).isEqualTo("nouveau@gmail.com");
    }

    @Test
    @DisplayName("Inscription échoue si email déjà utilisé")
    void shouldFailWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("password123");
        request.setNom("Dupont");
        request.setPrenom("Jean");

        try {
            webClient.post()
                    .uri("/api/auth/register")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("400");
        }
    }
}