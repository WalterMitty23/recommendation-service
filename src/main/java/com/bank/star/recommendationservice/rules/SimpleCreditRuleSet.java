package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;
    private final String productId;
    private final String productName;
    private final String productText;

    public SimpleCreditRuleSet(
            RecommendationsRepository repository,
            @Value("${products.simpleCredit.id}") String productId,
            @Value("${products.simpleCredit.name}") String productName,
            @Value("${products.simpleCredit.text}") String productText
    ) {
        this.repository = repository;
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        if (repository.userHasProductType(userId, "CREDIT")) {
            return Optional.empty();
        }

        long deposits = repository.sumDepositsByType(userId, "DEBIT");
        long withdrawals = repository.sumWithdrawalsByType(userId, "DEBIT");

        if (deposits <= withdrawals) {
            return Optional.empty();
        }
        if (withdrawals <= 100_000) {
            return Optional.empty();
        }

        return Optional.of(new RecommendationDto(
                UUID.fromString(productId),
                productName,
                productText
        ));
    }
}
