package com.bank.star.recommendationservice.service;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.rules.RecommendationRuleSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationRuleSet invest500RuleSet;

    @Mock
    private RecommendationRuleSet topSavingRuleSet;

    @Mock
    private RecommendationRuleSet simpleCreditRuleSet;

    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        recommendationService = new RecommendationService(
                List.of(invest500RuleSet, topSavingRuleSet, simpleCreditRuleSet)
        );
    }

    @Test
    void shouldReturnMultipleRecommendations_whenRulesSatisfied() {
        UUID userId = UUID.randomUUID();

        when(invest500RuleSet.evaluate(userId))
                .thenReturn(Optional.of(new RecommendationDto(UUID.randomUUID(), "Invest 500", "text")));
        when(topSavingRuleSet.evaluate(userId))
                .thenReturn(Optional.of(new RecommendationDto(UUID.randomUUID(), "Top Saving", "text")));
        when(simpleCreditRuleSet.evaluate(userId))
                .thenReturn(Optional.empty());

        List<RecommendationDto> recommendations = recommendationService.getRecommendations(userId);

        assertEquals(2, recommendations.size());
        assertTrue(recommendations.stream().anyMatch(r -> r.getName().equals("Invest 500")));
        assertTrue(recommendations.stream().anyMatch(r -> r.getName().equals("Top Saving")));

        verify(invest500RuleSet).evaluate(userId);
        verify(topSavingRuleSet).evaluate(userId);
        verify(simpleCreditRuleSet).evaluate(userId);
    }

    @Test
    void shouldReturnEmptyList_whenNoRulesSatisfied() {
        UUID userId = UUID.randomUUID();

        when(invest500RuleSet.evaluate(userId)).thenReturn(Optional.empty());
        when(topSavingRuleSet.evaluate(userId)).thenReturn(Optional.empty());
        when(simpleCreditRuleSet.evaluate(userId)).thenReturn(Optional.empty());

        List<RecommendationDto> recommendations = recommendationService.getRecommendations(userId);

        assertTrue(recommendations.isEmpty());
        verify(invest500RuleSet).evaluate(userId);
        verify(topSavingRuleSet).evaluate(userId);
        verify(simpleCreditRuleSet).evaluate(userId);
    }
}

