package com.bank.star.recommendationservice.rules;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Invest500RuleSetTest {

    private RecommendationsRepository repository;
    private Invest500RuleSet ruleSet;

    private final String productId = UUID.randomUUID().toString();
    private final String productName = "Invest 500";
    private final String productText = "Инвестиционный продукт";

    @BeforeEach
    void setUp() {
        repository = mock(RecommendationsRepository.class);
        ruleSet = new Invest500RuleSet(repository, productId, productName, productText);
    }

    @Test
    void shouldReturnRecommendation_ifConditionsAreMet() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.userHasProductType(userId, "INVEST")).thenReturn(false);
        when(repository.sumDepositsByType(userId, "SAVING")).thenReturn(1500);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isPresent());
        assertEquals(productName, result.get().getName());
    }

    @Test
    void shouldReturnEmpty_ifNoDebit() {
        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(false);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isEmpty());
        verify(repository).userHasProductType(userId, "DEBIT");
        verify(repository, never()).userHasProductType(userId, "INVEST");
    }
}
