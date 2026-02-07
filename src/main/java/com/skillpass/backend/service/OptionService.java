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

   // modifier une option
    public Option updateOption(Long id, Option updatedOption) {
        Option existing = optionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Option non trouvée: " + id));

        if (updatedOption.getTexte() != null) {
            existing.setTexte(updatedOption.getTexte());
        }

        if (updatedOption.isCorrecte() != existing.isCorrecte()) {
            Long questionId = existing.getQuestion().getId();
            long correctCount = optionRepository.countByQuestionIdAndCorrecteTrue(questionId);

            if (existing.isCorrecte() && correctCount <= 1) {
                throw new IllegalStateException(
                        "Impossible de marquer cette option comme incorrecte. " +
                                "Une question doit avoir au moins une bonne réponse."
                );
            }

            existing.setCorrecte(updatedOption.isCorrecte());
        }

        return optionRepository.save(existing);
    }

  // supprimer une option
    public void deleteOption(Long id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Option non trouvée: " + id));

        Long questionId = option.getQuestion().getId();
        long totalOptions = optionRepository.countByQuestionId(questionId);

        if (totalOptions <= 2) {
            throw new IllegalStateException(
                    "Impossible de supprimer cette option. " +
                            "Une question doit avoir au moins 2 options."
            );
        }

        if (option.isCorrecte()) {
            long correctCount = optionRepository.countByQuestionIdAndCorrecteTrue(questionId);
            if (correctCount <= 1) {
                throw new IllegalStateException(
                        "Impossible de supprimer cette option. " +
                                "C'est la seule bonne réponse de la question."
                );
            }
        }

        optionRepository.deleteById(id);
    }
}
