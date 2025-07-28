package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> check(UUID userId);
}
