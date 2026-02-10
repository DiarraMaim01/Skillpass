package com.skillpass.backend.controller;

import com.skillpass.backend.entity.Question;
import com.skillpass.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    //GET : liste des questions
    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);  // HTTP 200 OK
    }

    // GET : question par id
    @GetMapping("/{id}")
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
    public ResponseEntity<List<Question>> getQuestionsByCategory(
            @PathVariable Question.Categorie categorie,
            @RequestParam(defaultValue = "10") int count){

        List<Question> questions = questionService.getQuestionsByCategory(categorie, count);

        return ResponseEntity.ok(questions);
    }

    //POST : creer une question
    @PostMapping
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
    @PutMapping
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
    @DeleteMapping
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
    public ResponseEntity<String> getStats() {
        String stats = questionService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}
