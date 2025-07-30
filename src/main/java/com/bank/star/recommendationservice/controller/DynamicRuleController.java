package com.bank.star.recommendationservice.controller;

import com.bank.star.recommendationservice.dto.DynamicRuleRequest;
import com.bank.star.recommendationservice.dto.DynamicRuleResponse;
import com.bank.star.recommendationservice.service.DynamicRuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class DynamicRuleController {

    private final DynamicRuleService dynamicRuleService;

    public DynamicRuleController(DynamicRuleService dynamicRuleService) {
        this.dynamicRuleService = dynamicRuleService;
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
}
