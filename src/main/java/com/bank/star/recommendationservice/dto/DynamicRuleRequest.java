package com.bank.star.recommendationservice.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record DynamicRuleRequest(
        String product_name,
        UUID product_id,
        String product_text,
        List<Map<String, Object>> rule
) {}
