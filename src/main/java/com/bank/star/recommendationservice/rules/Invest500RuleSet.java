package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public Invest500RuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean valid = true;

        if (valid) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("147f6a0f-3b91-413b-ab99-87f081d60d5a"),
                    "Invest 500",
                    "Описание продукта Invest 500"
            ));
        }

        return Optional.empty();
    }
}
