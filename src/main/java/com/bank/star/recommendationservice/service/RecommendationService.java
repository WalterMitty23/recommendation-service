package com.bank.star.recommendationservice.service;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.entity.DynamicRuleEntity;
import com.bank.star.recommendationservice.rules.RecommendationRuleSet;
import com.bank.star.recommendationservice.repository.DynamicRuleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> rules;
    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleEvaluator dynamicRuleEvaluator;
    private final ObjectMapper objectMapper;

    public RecommendationService(
            List<RecommendationRuleSet> rules,
            DynamicRuleRepository dynamicRuleRepository,
            DynamicRuleEvaluator dynamicRuleEvaluator,
            ObjectMapper objectMapper) {
        this.rules = rules;
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.dynamicRuleEvaluator = dynamicRuleEvaluator;
        this.objectMapper = objectMapper;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = new ArrayList<>(rules.stream()
                .map(rule -> rule.evaluate(userId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());

        List<Map.Entry<DynamicRuleEntity, List<Map<String, Object>>>> dynamicRules =
                dynamicRuleRepository.findAll().stream()
                        .map(ruleEntity -> {
                            try {
                                List<Map<String, Object>> parsedRule = objectMapper.readValue(
                                        ruleEntity.getRuleJson(),
                                        new TypeReference<List<Map<String, Object>>>() {}
                                );
                                return Map.entry(ruleEntity, parsedRule);
                            } catch (Exception e) {
                                System.err.println("⚠ Пропущено повреждённое правило: " + ruleEntity.getId());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull) // убираем null, чтобы не упало
                        .toList();

        for (var entry : dynamicRules) {
            var entity = entry.getKey();
            var rule = entry.getValue();

            boolean matches = dynamicRuleEvaluator.evaluate(userId, rule);
            if (matches) {
                recommendations.add(new RecommendationDto(
                        entity.getProductId(),
                        entity.getProductName(),
                        entity.getProductText()
                ));
            }
        }

        return recommendations;
    }
}
