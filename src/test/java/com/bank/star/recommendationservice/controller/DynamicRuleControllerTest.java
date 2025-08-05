package com.bank.star.recommendationservice.controller;

import com.bank.star.recommendationservice.dto.DynamicRuleRequest;
import com.bank.star.recommendationservice.dto.DynamicRuleResponse;
import com.bank.star.recommendationservice.repository.DynamicRuleStatsRepository;
import com.bank.star.recommendationservice.service.DynamicRuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DynamicRuleController.class)
@ExtendWith(SpringExtension.class)
public class DynamicRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DynamicRuleService dynamicRuleService;

    @MockBean
    private DynamicRuleStatsRepository statsRepository;

    @Test
    void shouldReturnAllRules() throws Exception {
        when(dynamicRuleService.getAllRules()).thenReturn(List.of());

        mockMvc.perform(get("/rule"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldCreateRule() throws Exception {
        UUID productId = UUID.fromString("627a5da2-cf70-452c-a431-a6a379f40b36");

        DynamicRuleResponse response = new DynamicRuleResponse(
                productId,                         // этот ID должен совпадать с JSON
                "Sample Product",
                productId,
                "Sample Text",
                List.of(Map.of("key", "value"))
        );

        when(dynamicRuleService.createRule(any())).thenReturn(response);

        String requestJson = """
            {
              "product_id": "627a5da2-cf70-452c-a431-a6a379f40b36",
              "product_name": "Sample Product",
              "product_text": "Sample Text",
              "rule": []
            }
            """;

        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product_name").value("Sample Product"))
                .andExpect(jsonPath("$.product_id").value("627a5da2-cf70-452c-a431-a6a379f40b36"));
    }

    @Test
    void shouldDeleteRule() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(delete("/rule/" + productId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStats() throws Exception {
        when(statsRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/rule/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats").isArray());
    }
}
