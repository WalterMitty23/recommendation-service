package com.bank.star.recommendationservice.service;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.repository.DynamicRuleRepository;
import com.bank.star.recommendationservice.repository.DynamicRuleStatsRepository;
import com.bank.star.recommendationservice.rules.RecommendationRuleSet;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private RecommendationRuleSet rule1;

    @Mock
    private RecommendationRuleSet rule2;

    @Mock
    private DynamicRuleRepository dynamicRuleRepository;

    @Mock
    private DynamicRuleStatsRepository statsRepository;

    @Mock
    private DynamicRuleEvaluator evaluator;

    @Mock
    private ObjectMapper objectMapper;

    private RecommendationService service;

    @BeforeEach
    void setUp() {
        service = new RecommendationService(
                List.of(rule1, rule2),
                dynamicRuleRepository,
                statsRepository,
                evaluator,
                objectMapper
        );
    }

    @Test
    void shouldReturnRecommendations_whenRulesMatch() {
        UUID userId = UUID.randomUUID();

        when(rule1.evaluate(userId)).thenReturn(Optional.of(
                new RecommendationDto(UUID.randomUUID(), "Product A", "Description")
        ));
        when(rule2.evaluate(userId)).thenReturn(Optional.empty());

        List<RecommendationDto> result = service.getRecommendations(userId);

        assertEquals(1, result.size());
        assertEquals("Product A", result.get(0).getName());
    }

    @Test
    void shouldReturnEmptyList_whenNoRulesMatch() {
        UUID userId = UUID.randomUUID();

        when(rule1.evaluate(userId)).thenReturn(Optional.empty());
        when(rule2.evaluate(userId)).thenReturn(Optional.empty());

        List<RecommendationDto> result = service.getRecommendations(userId);

        assertTrue(result.isEmpty());
    }
}

