package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public SimpleCreditRuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> evaluate(UUID userId) {
        boolean noCredit = !repository.userHasProductType(userId, "CREDIT");

        boolean debitDepositsMoreThanSpend = repository.sumDepositsByType(userId, "DEBIT")
                > repository.sumWithdrawalsByType(userId, "DEBIT");

        boolean spendOver100k = repository.sumWithdrawalsByType(userId, "DEBIT") > 100_000;

        if (noCredit && debitDepositsMoreThanSpend && spendOver100k) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee864447f"),
                    "Простой кредит",
                    "Описание продукта Простой кредит"
            ));
        }

        return Optional.empty();
    }
}
