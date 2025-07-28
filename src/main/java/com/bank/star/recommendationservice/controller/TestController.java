package com.bank.star.recommendationservice.controller;

import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class TestController {

    private final RecommendationsRepository repository;

    public TestController(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/test/{userId}")
    public Map<String, Object> test(@PathVariable UUID userId) {
        Integer amount = repository.getRandomTransactionAmount(userId);
        return Map.of("userId", userId, "amount", amount);
    }
}
