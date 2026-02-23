package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Question;
import com.skillpass.backend.entity.Test;
import com.skillpass.backend.service.QuestionService;
import com.skillpass.backend.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@Tag(name = "Tests", description = "Gestion des tests d'évaluation")
@SecurityRequirement(name = "bearerAuth")
public class TestController {
    @Autowired
    private TestService testService;

    //GET : liste des tests
    @GetMapping
    @Operation(summary = "Lister tous les tests",
            description = "Retourne la liste de tous les tests disponibles (USER + ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<List<Test>> getAllTests() {
       List<Test> tests = testService.getTests();
        return ResponseEntity.ok(tests);
    }

    //GET : par id

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un test par son ID",
            description = "Retourne les détails d'un test spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Test trouvé"),
            @ApiResponse(responseCode = "404", description = "Test non trouvé"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
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
    @Operation(summary = "Rechercher des tests par titre",
            description = "Recherche des tests dont le titre contient le mot-clé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultats de la recherche"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<List<Test>> searchTests(@RequestParam String titre) {
        List<Test> tests = testService.findTestByTitre(titre);
        return ResponseEntity.ok(tests);
    }

    // POST : creer un test aleatoire
    @PostMapping("/random")
    @Operation(summary = "Créer un test aléatoire",
            description = "Génère un test avec des questions aléatoires d'une catégorie (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Test créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Paramètres invalides"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
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
    @Operation(summary = "Ajouter une question à un test",
            description = "Ajoute une question existante à un test (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question ajoutée"),
            @ApiResponse(responseCode = "404", description = "Test ou question non trouvé"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
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
    @Operation(summary = "Calculer un score",
            description = "Calcule le score d'un utilisateur pour un test donné (USER + ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Score calculé"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
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
    @Operation(summary = "Retirer une question d'un test",
            description = "Retire une question d'un test (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question retirée"),
            @ApiResponse(responseCode = "404", description = "Test ou question non trouvé"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
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

    //DELETE : supprimer un test
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un test",
            description = "Supprime un test par son ID (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Test supprimé"),
            @ApiResponse(responseCode = "404", description = "Test non trouvé"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        try {
            testService.deleteTest(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }




}
