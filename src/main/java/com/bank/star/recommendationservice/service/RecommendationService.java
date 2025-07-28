package com.bank.star.recommendationservice.service;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> rules;

    public RecommendationService(List<RecommendationRuleSet> rules) {
        this.rules = rules;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        return rules.stream()
                .map(rule -> rule.evaluate(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
