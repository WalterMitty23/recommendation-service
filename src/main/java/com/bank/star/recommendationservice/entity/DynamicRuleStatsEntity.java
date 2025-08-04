package com.bank.star.recommendationservice.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "rule_stats")
public class DynamicRuleStatsEntity {

    @Id
    @Column(name = "rule_id")
    private UUID ruleId;

    @Column(name = "count", nullable = false)
    private long count;

    public DynamicRuleStatsEntity() {}

    public DynamicRuleStatsEntity(UUID ruleId, long count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
