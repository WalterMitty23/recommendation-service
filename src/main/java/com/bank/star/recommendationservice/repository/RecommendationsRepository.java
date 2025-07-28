package com.bank.star.recommendationservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer getRandomTransactionAmount(UUID userId) {
        return jdbcTemplate.queryForObject(
                "SELECT amount FROM transactions WHERE user_id = ? LIMIT 1",
                Integer.class,
                userId
        );
    }
}
