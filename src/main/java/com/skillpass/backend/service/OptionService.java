package com.skillpass.backend.service;

import com.skillpass.backend.entity.Option;
import com.skillpass.backend.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    @Autowired
    OptionRepository optionRepository;

// valider une reponse
    public boolean validateAnswer(Long OptionId){
        Option option = optionRepository.findById(OptionId)
                .orElseThrow(() -> new IllegalArgumentException("Option non trouvée"));
        return option.isCorrecte();
    }


    // Obtenir les bonnes reponses
    public List<Option> getCorrectOptions(Long questionId){
        return optionRepository.findByQuestionIdAndCorrecteTrue(questionId);
    }

  // nombre de reponses et de reponses correctes par questions
    public String getAnswerStats(Long questionId) {
        long totalOptions = optionRepository.countByQuestionId(questionId);
        long correctOptions = optionRepository.countByQuestionIdAndCorrecteTrue(questionId);

        return String.format(
                " Question ID %d : %d options total, %d correctes",
                questionId, totalOptions, correctOptions
        );
    }
}
