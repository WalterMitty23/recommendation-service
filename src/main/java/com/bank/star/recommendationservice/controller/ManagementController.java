package com.bank.star.recommendationservice.controller;

import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/management")
public class ManagementController {

    private final CacheManager cacheManager;

    public ManagementController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @PostMapping("/clear-caches")
    public void clearCaches() {
        cacheManager.getCacheNames().forEach(
                name -> Objects.requireNonNull(cacheManager.getCache(name)).clear()
        );
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of(
                "name", "recommendation-service",
                "version", "0.0.1-SNAPSHOT"
        );
    }
}
