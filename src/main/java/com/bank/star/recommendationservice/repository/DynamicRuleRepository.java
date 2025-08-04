package com.bank.star.recommendationservice.repository;

import com.bank.star.recommendationservice.entity.DynamicRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DynamicRuleRepository extends JpaRepository<DynamicRuleEntity, UUID> {

    void deleteByProductId(UUID productId);

}
