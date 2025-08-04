package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TopSavingRuleSetTest {

    private RecommendationsRepository repository;
    private TopSavingRuleSet ruleSet;

    private final String productId = UUID.randomUUID().toString();
    private final String productName = "Top Saving";
    private final String productText = "Сберегательный продукт";

    @BeforeEach
    void setUp() {
        repository = mock(RecommendationsRepository.class);
        ruleSet = new TopSavingRuleSet(repository, productId, productName, productText);
    }

    @Test
    void shouldReturnRecommendation_ifAllConditionsMet() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.sumDepositsByType(userId, "DEBIT")).thenReturn(60_000);
        when(repository.sumDepositsByType(userId, "SAVING")).thenReturn(20_000);
        when(repository.sumWithdrawalsByType(userId, "DEBIT")).thenReturn(10_000);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isPresent());
        assertEquals(productName, result.get().getName());
    }

    @Test
    void shouldReturnEmpty_ifNoDebitProduct() {
        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(false);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isEmpty());
    }
}
