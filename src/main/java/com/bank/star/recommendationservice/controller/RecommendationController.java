package com.bank.star.recommendationservice.controller;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public Map<String, Object> getRecommendations(@PathVariable UUID userId) {
        List<RecommendationDto> recommendations = service.getRecommendations(userId);

        return Map.of(
                "user_id", userId,
                "recommendations", recommendations
        );
    }
}
