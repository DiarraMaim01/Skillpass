package com.skillpass.backend.controller;

import com.skillpass.backend.repository.QuestionRepository;
import com.skillpass.backend.repository.TestRepository;
import com.skillpass.backend.repository.UserRepository;
import com.skillpass.backend.repository.TestResultRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Dashboard administrateur")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @GetMapping("/stats")
    @Operation(summary = "Statistiques globales")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalQuestions", questionRepository.count());
        stats.put("totalTests", testRepository.count());
        stats.put("totalUsers", userRepository.count());
        stats.put("totalResults", testResultRepository.count());
        return ResponseEntity.ok(stats);
    }
}