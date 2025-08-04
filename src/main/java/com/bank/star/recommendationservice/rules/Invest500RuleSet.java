package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;
    private final String productId;
    private final String productName;
    private final String productText;

    public Invest500RuleSet(
            RecommendationsRepository repository,
            @Value("${products.invest500.id}") String productId,
            @Value("${products.invest500.name}") String productName,
            @Value("${products.invest500.text}") String productText
    ) {
        this.repository = repository;
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        if (!repository.userHasProductType(userId, "DEBIT")) {
            return Optional.empty();
        }
        if (repository.userHasProductType(userId, "INVEST")) {
            return Optional.empty();
        }
        if (repository.sumDepositsByType(userId, "SAVING") <= 1000) {
            return Optional.empty();
        }

        return Optional.of(new RecommendationDto(
                UUID.fromString(productId),
                productName,
                productText
        ));
    }
}

