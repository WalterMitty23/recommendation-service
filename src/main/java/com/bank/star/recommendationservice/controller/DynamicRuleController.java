package com.bank.star.recommendationservice.controller;

import com.bank.star.recommendationservice.dto.DynamicRuleRequest;
import com.bank.star.recommendationservice.dto.DynamicRuleResponse;
import com.bank.star.recommendationservice.entity.DynamicRuleStatsEntity;
import com.bank.star.recommendationservice.repository.DynamicRuleStatsRepository;
import com.bank.star.recommendationservice.service.DynamicRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/rule")
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;
    private final DynamicRuleStatsRepository statsRepository;

    public DynamicRuleController(DynamicRuleService dynamicRuleService,
                                 DynamicRuleStatsRepository statsRepository) {
        this.dynamicRuleService = dynamicRuleService;
        this.statsRepository = statsRepository;
    }

    @PostMapping
    public ResponseEntity<DynamicRuleResponse> createRule(@RequestBody DynamicRuleRequest request) {
        DynamicRuleResponse savedRule = dynamicRuleService.createRule(request);
        return ResponseEntity.status(HttpStatus.OK).body(savedRule);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<DynamicRuleResponse>>> getAllRules() {
        List<DynamicRuleResponse> rules = dynamicRuleService.getAllRules();
        return ResponseEntity.ok(Collections.singletonMap("data", rules));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        dynamicRuleService.deleteRuleByProductId(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<Map<String, Object>> stats = statsRepository.findAll()
                .stream()
                .map(stat -> Map.<String, Object>of(
                        "rule_id", stat.getRuleId(),
                        "count", stat.getCount()
                ))
                .toList();

        return Map.of("stats", stats);
    }
}
