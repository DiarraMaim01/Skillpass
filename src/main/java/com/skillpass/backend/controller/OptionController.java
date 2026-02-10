package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.entity.Question;
import com.skillpass.backend.service.OptionService;
import com.skillpass.backend.service.QuestionService;
import com.skillpass.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/options")
public class OptionController {

    @Autowired
    private OptionService optionService;

    //POST : valider une reponse
    @PostMapping("/{id}/validate")
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
    public ResponseEntity<List<Option>> getCorrectAnswers(@PathVariable Long questionId) {
        List<Option> correctOptions = optionService.getCorrectOptions(questionId);
        return ResponseEntity.ok(correctOptions);
    }

    // PUT : mettre a jour une option
    @PutMapping("/{id}")
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
    public ResponseEntity<String> getAnswerStats(@PathVariable Long questionId) {
        String stats = optionService.getAnswerStats(questionId);
        return ResponseEntity.ok(stats);
    }

    // DELETE : supprimer une option
    @DeleteMapping("/{id}")
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
