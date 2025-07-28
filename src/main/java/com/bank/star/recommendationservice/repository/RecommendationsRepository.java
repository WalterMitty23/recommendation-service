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

    public boolean userHasProductType(UUID userId, String productType) {
        String sql = """
            SELECT COUNT(*) 
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ?
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
        return count != null && count > 0;
    }

    public int sumDepositsByType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount),0) 
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }

    public int sumWithdrawalsByType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount),0) 
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }
}
