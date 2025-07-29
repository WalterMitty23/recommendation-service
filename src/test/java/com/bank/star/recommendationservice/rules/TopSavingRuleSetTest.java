package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopSavingRuleSetTest {

    @Mock
    private RecommendationsRepository repository;

    private TopSavingRuleSet ruleSet;

    private String productId;
    private String productName;
    private String productText;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID().toString();
        productName = "Top Saving";
        productText = "Текст рекомендации";
        ruleSet = new TopSavingRuleSet(repository, productId, productName, productText);
    }

    @Test
    void shouldReturnRecommendation_whenAllConditionsMet_caseDebitDeposits() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.sumDepositsByType(userId, "DEBIT")).thenReturn(60_000);
        when(repository.sumDepositsByType(userId, "SAVING")).thenReturn(20_000);
        when(repository.sumWithdrawalsByType(userId, "DEBIT")).thenReturn(30_000);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isPresent());
        assertEquals(productName, result.get().getName());
    }

    @Test
    void shouldReturnRecommendation_whenAllConditionsMet_caseSavingDeposits() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.sumDepositsByType(userId, "DEBIT")).thenReturn(40_000);
        when(repository.sumDepositsByType(userId, "SAVING")).thenReturn(60_000);
        when(repository.sumWithdrawalsByType(userId, "DEBIT")).thenReturn(30_000);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isPresent());
        assertEquals(productName, result.get().getName());
    }

    @Test
    void shouldReturnEmpty_whenNoDebit() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(false);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isEmpty());
    }
}
