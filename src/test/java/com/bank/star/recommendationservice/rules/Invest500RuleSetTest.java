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
class Invest500RuleSetTest {

    @Mock
    private RecommendationsRepository repository;

    private Invest500RuleSet ruleSet;

    private String productId;
    private String productName;
    private String productText;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID().toString();
        productName = "Invest 500";
        productText = "Special investment product";

        // Создаём RuleSet с тестовыми значениями
        ruleSet = new Invest500RuleSet(repository, productId, productName, productText);
    }

    @Test
    void shouldReturnRecommendation_whenAllConditionsMet() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.userHasProductType(userId, "INVEST")).thenReturn(false);
        when(repository.sumDepositsByType(userId, "SAVING")).thenReturn(2000);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isPresent());
        RecommendationDto dto = result.get();

        assertEquals(UUID.fromString(productId), dto.getId());
        assertEquals(productName, dto.getName());
        assertEquals(productText, dto.getText());

        verify(repository).userHasProductType(userId, "DEBIT");
        verify(repository).userHasProductType(userId, "INVEST");
        verify(repository).sumDepositsByType(userId, "SAVING");
    }

    @Test
    void shouldReturnEmpty_whenConditionsNotMet() {
        UUID userId = UUID.randomUUID();

        // Нарушаем первое условие (нет DEBIT)
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(false);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertFalse(result.isPresent());

        verify(repository).userHasProductType(userId, "DEBIT");
        // проверяем, что после false программа завершила проверку и не пошла дальше
        verify(repository, never()).userHasProductType(userId, "INVEST");
        verify(repository, never()).sumDepositsByType(userId, "SAVING");
    }
}
