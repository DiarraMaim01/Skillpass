package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Question;
import com.skillpass.backend.service.QuestionService;
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
@RequestMapping("/api/questions")
@Tag(name = "Questions", description = "Gestion des questions de test")
@SecurityRequirement(name = "bearerAuth")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    //GET : liste des questions
    @GetMapping
    @Operation(summary = "Lister toutes les questions",
            description = "Retourne la liste complète des questions (USER + ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }

    // GET : question par id
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une question par son ID",
            description = "Retourne les détails d'une question spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question trouvée"),
            @ApiResponse(responseCode = "404", description = "Question non trouvée - l'ID n'existe pas"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        try{
            Question question = questionService.getQuestionById(id);
            return ResponseEntity.ok(question);
        }catch(IllegalArgumentException e){
           return ResponseEntity.notFound().build(); // HTTP 404 not found
        }
    }

    // GET : questions aleatoires
    @GetMapping("/random")
    @Operation(summary = "Récupérer une question aléatoire",
            description = "Retourne une question aléatoire parmi la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question aléatoire récupérée"),
            @ApiResponse(responseCode = "400", description = "Paramètre invalide - count doit être entre 1 et 50"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<Question> getRandomQuestion(
        @RequestParam(defaultValue = "10") int count){
        if (count <= 0 || count > 50) {
            return ResponseEntity.badRequest().build();// HTTP 400 Bad Request
        }
        List<Question> questions = questionService.getRandomQuestions(count);

        return ResponseEntity.ok(questions.get(0));

    }

    // GET : question par categorie
    @GetMapping("/category/{categorie}")
    @Operation(summary = "Récupérer des questions par catégorie",
            description = "Retourne une liste de questions appartenant à une catégorie spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Questions récupérées"),
            @ApiResponse(responseCode = "400", description = "Catégorie invalide"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<List<Question>> getQuestionsByCategory(
            @PathVariable Question.Categorie categorie,
            @RequestParam(defaultValue = "10") int count){

        List<Question> questions = questionService.getQuestionsByCategory(categorie, count);

        return ResponseEntity.ok(questions);
    }

    //POST : creer une question
    @PostMapping
    @Operation(summary = "Créer une question",
            description = "Crée une nouvelle question (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        try {
            Question savedQuestion = questionService.createQuestion(question);
            return ResponseEntity.status(HttpStatus.CREATED)  // HTTP 201
                    .body(savedQuestion);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();  // HTTP 400
        }
    }

    // PUT : modifier une question
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une question",
            description = "Modifie une question existante (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Question non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
    public ResponseEntity<Question> updateQuestion(
            @PathVariable Long id ,
            @RequestBody Question question) {
        try {
            Question updatedQuestion = questionService.updateQuestion(id, question);
            return ResponseEntity.ok(updatedQuestion);
        }catch(IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }

    }

    // DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une question",
            description = "Supprime une question par son ID (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question supprimée"),
            @ApiResponse(responseCode = "404", description = "Question non trouvée"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.noContent().build();  // HTTP 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();  // HTTP 404
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // HTTP 409 Conflict
        }
    }

    //GET : STATS
    @GetMapping("/stats")
    @Operation(summary = "Obtenir des statistiques",
            description = "Retourne des statistiques sur les questions (nombre total, par catégorie)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques récupérées"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<String> getStats() {
        String stats = questionService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}
