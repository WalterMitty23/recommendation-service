package com.bank.star.recommendationservice.controller;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.service.RecommendationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecommendationController.class)
@ExtendWith(SpringExtension.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @Test
    void shouldReturnRecommendations_whenUserHasRecommendations() throws Exception {
        UUID userId = UUID.randomUUID();
        RecommendationDto dto = new RecommendationDto(
                UUID.randomUUID(),
                "Top Saving",
                "Описание продукта"
        );

        when(recommendationService.getRecommendations(userId))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/recommendation/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user_id", is(userId.toString())))
                .andExpect(jsonPath("$.recommendations", hasSize(1)))
                .andExpect(jsonPath("$.recommendations[0].name", is("Top Saving")));
    }

    @Test
    void shouldReturnEmptyRecommendations_whenNoneAvailable() throws Exception {
        UUID userId = UUID.randomUUID();

        when(recommendationService.getRecommendations(userId)).thenReturn(List.of());

        mockMvc.perform(get("/recommendation/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_id", is(userId.toString())))
                .andExpect(jsonPath("$.recommendations", hasSize(0)));
    }
}
