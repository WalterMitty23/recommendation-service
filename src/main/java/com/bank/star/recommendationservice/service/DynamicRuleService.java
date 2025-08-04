package com.bank.star.recommendationservice.service;

import com.bank.star.recommendationservice.dto.DynamicRuleRequest;
import com.bank.star.recommendationservice.dto.DynamicRuleResponse;
import com.bank.star.recommendationservice.entity.DynamicRuleEntity;
import com.bank.star.recommendationservice.repository.DynamicRuleRepository;
import com.bank.star.recommendationservice.repository.DynamicRuleStatsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleService {

    private final DynamicRuleRepository dynamicRuleRepository;
    private final DynamicRuleStatsRepository statsRepository;
    private final ObjectMapper objectMapper;

    public DynamicRuleService(
            DynamicRuleRepository dynamicRuleRepository,
            DynamicRuleStatsRepository statsRepository,
            ObjectMapper objectMapper) {
        this.dynamicRuleRepository = dynamicRuleRepository;
        this.statsRepository = statsRepository;
        this.objectMapper = objectMapper;
    }

    public DynamicRuleResponse createRule(DynamicRuleRequest request) {
        try {
            String ruleJson = objectMapper.writeValueAsString(request.rule());
            DynamicRuleEntity entity = new DynamicRuleEntity();
            entity.setProductName(request.product_name());
            entity.setProductId(request.product_id());
            entity.setProductText(request.product_text());
            entity.setRuleJson(ruleJson);

            DynamicRuleEntity saved = dynamicRuleRepository.save(entity);
            return mapToResponse(saved);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка сериализации правила в JSON", e);
        }
    }

    public List<DynamicRuleResponse> getAllRules() {
        return dynamicRuleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public void deleteRuleByProductId(UUID productId) {
        dynamicRuleRepository.findAll()
                .stream()
                .filter(rule -> rule.getProductId().equals(productId))
                .forEach(rule -> statsRepository.deleteByRuleId(rule.getId()));

        dynamicRuleRepository.deleteByProductId(productId);
    }

    private DynamicRuleResponse mapToResponse(DynamicRuleEntity entity) {
        try {
            List<?> rule = objectMapper.readValue(entity.getRuleJson(), List.class);
            return new DynamicRuleResponse(
                    entity.getId(),
                    entity.getProductName(),
                    entity.getProductId(),
                    entity.getProductText(),
                    (List) rule
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка десериализации JSON в правило", e);
        }
    }
}
