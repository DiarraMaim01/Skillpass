package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.entity.Question;
import com.skillpass.backend.service.OptionService;
import com.skillpass.backend.service.QuestionService;
import com.skillpass.backend.service.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@Tag(name = "Options", description = "Gestion des options de réponse")
@SecurityRequirement(name = "bearerAuth")
public class OptionController {

    @Autowired
    private OptionService optionService;

    //POST : valider une reponse
    @PostMapping("/{id}/validate")
    @Operation(summary = "Valider une réponse",
            description = "Vérifie si une option est correcte (USER + ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultat de la validation"),
            @ApiResponse(responseCode = "404", description = "Option non trouvée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<Boolean> validateAnswer(@PathVariable Long id) {
        try {
            boolean isCorrect = optionService.validateAnswer(id);
            return ResponseEntity.ok(isCorrect);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //GET : bonnes reponses
    @GetMapping("/question/{questionId}/correct")
    @Operation(summary = "Récupérer les bonnes réponses",
            description = "Retourne toutes les options correctes d'une question (USER + ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des bonnes réponses"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<List<Option>> getCorrectAnswers(@PathVariable Long questionId) {
        List<Option> correctOptions = optionService.getCorrectOptions(questionId);
        return ResponseEntity.ok(correctOptions);
    }

    // PUT : mettre a jour une option
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une option",
            description = "Modifie une option existante (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Option mise à jour"),
            @ApiResponse(responseCode = "404", description = "Option non trouvée"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
    public ResponseEntity<Option> updateOption(
            @PathVariable Long id,
            @RequestBody Option option) {

        try {
            Option updated = optionService.updateOption(id, option);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }



    // GET : stats

    @GetMapping("/question/{questionId}/stats")
    @Operation(summary = "Statistiques d'une question",
            description = "Retourne des statistiques sur les options d'une question (USER + ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistiques"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<String> getAnswerStats(@PathVariable Long questionId) {
        String stats = optionService.getAnswerStats(questionId);
        return ResponseEntity.ok(stats);
    }

    // DELETE : supprimer une option
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une option",
            description = "Supprime une option par son ID (ADMIN uniquement)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Option supprimée"),
            @ApiResponse(responseCode = "404", description = "Option non trouvée"),
            @ApiResponse(responseCode = "400", description = "Impossible de supprimer (dernière option ou dernière bonne réponse)"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé - rôle ADMIN requis")
    })
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        try {
            optionService.deleteOption(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
