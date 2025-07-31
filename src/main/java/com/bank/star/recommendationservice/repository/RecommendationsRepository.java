package com.bank.star.recommendationservice.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
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


    @Cacheable(value = "userHasProductType", key = "#userId + '-' + #productType")
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


    @Cacheable(value = "sumDepositsByType", key = "#userId + '-' + #productType")
    public int sumDepositsByType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount),0) 
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = 'DEPOSIT'
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }

    @Cacheable(value = "sumWithdrawalsByType", key = "#userId + '-' + #productType")
    public int sumWithdrawalsByType(UUID userId, String productType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount),0) 
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = 'WITHDRAW'
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }

    @Cacheable(value = "countTransactionsByType", key = "#userId + '-' + #productType")
    public int countTransactionsByType(UUID userId, String productType) {
        String sql = """
            SELECT COUNT(*)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
    }

    @Cacheable(value = "sumTransactionByType", key = "#userId + '-' + #productType + '-' + #txType")
    public int sumTransactionByType(UUID userId, String productType, String txType) {
        String sql = """
            SELECT COALESCE(SUM(t.amount),0)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ? AND t.type = ?
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, productType, txType);
    }
    public Optional<UUID> findUserIdByFullName(String fullName) {
        String sql = """
        SELECT id FROM users 
        WHERE CONCAT(first_name, ' ', last_name) = ?
    """;

        return jdbcTemplate.query(sql, ps -> ps.setString(1, fullName), rs -> {
            if (!rs.next()) {
                return Optional.empty();
            }
            UUID userId = UUID.fromString(rs.getString("id"));
            if (rs.next()) {
                return Optional.empty();
            }
            return Optional.of(userId);
        });
    }
}
