package com.bank.star.recommendationservice.repository;

import com.bank.star.recommendationservice.entity.DynamicRuleStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface DynamicRuleStatsRepository extends JpaRepository<DynamicRuleStatsEntity, UUID> {

    @Modifying
    @Transactional
    @Query("UPDATE DynamicRuleStatsEntity s SET s.count = s.count + 1 WHERE s.ruleId = :ruleId")
    int incrementCount(@Param("ruleId") UUID ruleId);

    @Modifying
    @Transactional
    void deleteByRuleId(UUID ruleId);
}
