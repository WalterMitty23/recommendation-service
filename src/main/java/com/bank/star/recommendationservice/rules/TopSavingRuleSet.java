package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public TopSavingRuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean hasDebit = repository.userHasProductType(userId, "DEBIT");

        boolean savingOrDebitSum = repository.sumDepositsByType(userId, "DEBIT") >= 50_000
                || repository.sumDepositsByType(userId, "SAVING") >= 50_000;

        boolean debitDepositsMoreThanSpend = repository.sumDepositsByType(userId, "DEBIT")
                > repository.sumWithdrawalsByType(userId, "DEBIT");

        if (hasDebit && savingOrDebitSum && debitDepositsMoreThanSpend) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                    "Top Saving",
                    "Описание продукта Top Saving"
            ));
        }

        return Optional.empty();
    }
}
