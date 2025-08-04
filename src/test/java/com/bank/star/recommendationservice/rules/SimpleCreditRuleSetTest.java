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
class SimpleCreditRuleSetTest {

    @Mock
    private RecommendationsRepository repository;

    private SimpleCreditRuleSet ruleSet;

    private String productId;
    private String productName;
    private String productText;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID().toString();
        productName = "Простой кредит";
        productText = "Откройте мир выгодных кредитов!";
        // создаём RuleSet руками, передаём тестовые значения
        ruleSet = new SimpleCreditRuleSet(repository, productId, productName, productText);
    }

    @Test
    void shouldReturnRecommendation_whenAllConditionsMet() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "CREDIT")).thenReturn(false);
        when(repository.sumDepositsByType(userId, "DEBIT")).thenReturn(150_000);
        when(repository.sumWithdrawalsByType(userId, "DEBIT")).thenReturn(110_000);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isPresent());
        RecommendationDto dto = result.get();
        assertEquals(UUID.fromString(productId), dto.getId());
        assertEquals(productName, dto.getName());
        assertEquals(productText, dto.getText());

        verify(repository).userHasProductType(userId, "CREDIT");
        verify(repository).sumDepositsByType(userId, "DEBIT");
        verify(repository).sumWithdrawalsByType(userId, "DEBIT");
    }

    @Test
    void shouldReturnEmpty_whenHasCredit() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "CREDIT")).thenReturn(true);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isEmpty());
        verify(repository).userHasProductType(userId, "CREDIT");
        verify(repository, never()).sumDepositsByType(any(), anyString());
    }

    @Test
    void shouldReturnEmpty_whenDepositsLessOrEqualWithdrawals() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "CREDIT")).thenReturn(false);
        when(repository.sumDepositsByType(userId, "DEBIT")).thenReturn(50_000);
        when(repository.sumWithdrawalsByType(userId, "DEBIT")).thenReturn(60_000);

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmpty_whenWithdrawalsLessOrEqual100k() {
        UUID userId = UUID.randomUUID();

        when(repository.userHasProductType(userId, "CREDIT")).thenReturn(false);
        when(repository.sumDepositsByType(userId, "DEBIT")).thenReturn(150_000);
        when(repository.sumWithdrawalsByType(userId, "DEBIT")).thenReturn(100_000); // <= 100k

        Optional<RecommendationDto> result = ruleSet.evaluate(userId);

        assertTrue(result.isEmpty());
    }
}
