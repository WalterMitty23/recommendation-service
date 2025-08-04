package com.bank.star.recommendationservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonProperty("product_name")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @JsonProperty("product_id")
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @JsonProperty("product_text")
    @Lob
    @Column(name = "product_text", nullable = false)
    private String productText;

    @JsonProperty("rule")
    @Lob
    @Column(name = "rule_json", columnDefinition = "TEXT")
    private String ruleJson;
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }

    public String getRuleJson() { return ruleJson; }
    public void setRuleJson(String ruleJson) { this.ruleJson = ruleJson; }
}
