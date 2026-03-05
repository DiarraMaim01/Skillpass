package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Test;
import com.skillpass.backend.entity.TestResult;
import com.skillpass.backend.entity.User;
import com.skillpass.backend.repository.TestRepository;
import com.skillpass.backend.repository.TestResultRepository;
import com.skillpass.backend.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@Tag(name = "Résultats", description = "Gestion des résultats de tests")
@SecurityRequirement(name = "bearerAuth")
public class TestResultController {

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserRepository userRepository;

    // POST : sauvegarder un résultat
    @PostMapping
    public ResponseEntity<TestResult> saveResult(
            @RequestBody Map<String, Object> body,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User non trouvé"));

            Long testId = Long.valueOf(body.get("testId").toString());
            Test test = testRepository.findById(testId)
                    .orElseThrow(() -> new IllegalArgumentException("Test non trouvé"));

            TestResult result = new TestResult();
            result.setUser(user);
            result.setTest(test);
            result.setScore(Integer.parseInt(body.get("score").toString()));
            result.setTotalQuestions(Integer.parseInt(body.get("totalQuestions").toString()));
            result.setCorrectAnswers(Integer.parseInt(body.get("correctAnswers").toString()));
            result.setTimeSpentSeconds(Integer.parseInt(body.get("timeSpentSeconds").toString()));
            result.setTotalPoints(Integer.parseInt(body.get("totalPoints").toString()));

            TestResult saved = testResultRepository.save(result);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET : résultats de l'utilisateur connecté
    @GetMapping("/me")
    public ResponseEntity<List<Map<String, Object>>> getMyResults(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User non trouvé"));

            List<TestResult> results = testResultRepository.findByUserId(user.getId());

            List<Map<String, Object>> response = results.stream().map(r -> {
                Map<String, Object> map = new java.util.LinkedHashMap<>();
                map.put("id", r.getId());
                map.put("score", r.getScore());
                map.put("totalQuestions", r.getTotalQuestions());
                map.put("correctAnswers", r.getCorrectAnswers());
                map.put("timeSpentSeconds", r.getTimeSpentSeconds());
                map.put("completedAt", r.getCompletedAt());
                map.put("percentage", r.getPercentage());
                // test info
                if (r.getTest() != null) {
                    Map<String, Object> testMap = new java.util.LinkedHashMap<>();
                    testMap.put("id", r.getTest().getId());
                    testMap.put("titre", r.getTest().getTitre());
                    map.put("test", testMap);
                }
                return map;
            }).collect(java.util.stream.Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}