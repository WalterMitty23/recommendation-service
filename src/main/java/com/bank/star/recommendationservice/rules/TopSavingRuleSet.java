package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;
    private final String productId;
    private final String productName;
    private final String productText;

    public TopSavingRuleSet(
            RecommendationsRepository repository,
            @Value("${products.topSaving.id}") String productId,
            @Value("${products.topSaving.name}") String productName,
            @Value("${products.topSaving.text}") String productText
    ) {
        this.repository = repository;
        this.productId = productId;
        this.productName = productName;
        this.productText = productText;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasDebit = repository.userHasProductType(userId, "DEBIT");

        long debitDeposits = repository.sumDepositsByType(userId, "DEBIT");
        long savingDeposits = repository.sumDepositsByType(userId, "SAVING");
        long debitWithdrawals = repository.sumWithdrawalsByType(userId, "DEBIT");

        boolean savingOrDebitSum = debitDeposits >= 50_000 || savingDeposits >= 50_000;
        boolean debitDepositsMoreThanSpend = debitDeposits > debitWithdrawals;

        if (hasDebit && savingOrDebitSum && debitDepositsMoreThanSpend) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString(productId),
                    productName,
                    productText
            ));
        }

        return Optional.empty();
    }
}
