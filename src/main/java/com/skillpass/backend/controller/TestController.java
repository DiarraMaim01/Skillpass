package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Question;
import com.skillpass.backend.entity.Test;
import com.skillpass.backend.service.QuestionService;
import com.skillpass.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {
    @Autowired
    private TestService testService;

    //GET : liste des tests
    @GetMapping
    public ResponseEntity<List<Test>> getAllTests() {
       List<Test> tests = testService.getTests();
        return ResponseEntity.ok(tests);
    }

    //GET : par id
    @GetMapping("/{id}")
    public ResponseEntity<Test> getTest(@PathVariable Long id) {
        try {
            Test test = testService.getTestById(id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //GET : test par titre
    @GetMapping("/search")
    public ResponseEntity<List<Test>> searchTests(@RequestParam String titre) {
        List<Test> tests = testService.findTestByTitre(titre);
        return ResponseEntity.ok(tests);
    }

    // POST : creer un test aleatoire
    @PostMapping("/random")
    public ResponseEntity<Test> createRandomTests(
        @RequestParam String titre,
        @RequestParam String description,
        @RequestParam Question.Categorie categorie,
        @RequestParam(defaultValue = "10") int questionCount) {

            try {
                Test test = testService.createRandomTest(titre, description, categorie, questionCount);
                return ResponseEntity.status(HttpStatus.CREATED).body(test);
            } catch (Exception e) {
                return ResponseEntity.badRequest().build();
            }
    }

    //POST : ajouter une question a un test
    @PostMapping("/{testId}/questions/{questionId}")
    public ResponseEntity<Test> addQuestionToTest(
            @PathVariable Long testId,
            @PathVariable Long questionId) {

        try {
            Test updatedTest = testService.addQuestionToTest(testId, questionId);
            return ResponseEntity.ok(updatedTest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //POST : calculer le score
    @PostMapping("/{testId}/calculate-score")
    public ResponseEntity<String> calculateScore(
            @PathVariable Long testId,
            @RequestBody List<Long> selectedOptionIds) {

        try {
            Test test = testService.getTestById(testId);
            return ResponseEntity.ok("Score calculé: 15/20");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE :retirer une question d'un test

    @DeleteMapping("/{testId}/questions/{questionId}")
    public ResponseEntity<Test> removeQuestionFromTest(
            @PathVariable Long testId,
            @PathVariable Long questionId) {

        try {
            Test updatedTest = testService.removeQuestionFromTest(testId, questionId);
            return ResponseEntity.ok(updatedTest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE : supprimer une question
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        try {
            testService.deleteTest(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }




}
