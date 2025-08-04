package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimpleCreditRuleSetTest {

    private RecommendationsRepository repository;
    private SimpleCreditRuleSet ruleSet;

    private final String productId = UUID.randomUUID().toString();
    private final String productName = "Простой кредит";
    private final String productText = "Кредитный продукт";

    @BeforeEach
    void setUp() {
        repository = mock(RecommendationsRepository.class);
        ruleSet = new SimpleCreditRuleSet(repository, productId, productName, productText);
    }

    @Test
    void shouldReturnRecommendation_whenValidUser() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "CREDIT")).thenReturn(false);
        when(repository.sumDepositsByType(userId, "DEBIT")).thenReturn(150_000);
        when(repository.sumWithdrawalsByType(userId, "DEBIT")).thenReturn(120_000);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isPresent());
        assertEquals(productName, result.get().getName());
    }

    @Test
    void shouldReturnEmpty_whenUserAlreadyHasCredit() {
        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "CREDIT")).thenReturn(true);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isEmpty());
    }
}
