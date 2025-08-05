package com.bank.star.recommendationservice.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManagementController.class)
@ExtendWith(SpringExtension.class)
class ManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CacheManager cacheManager;

    @Test
    void shouldClearCaches() throws Exception {
        Cache mockCache = mock(Cache.class);

        when(cacheManager.getCacheNames()).thenReturn(List.of("testCache"));
        when(cacheManager.getCache("testCache")).thenReturn(mockCache);

        mockMvc.perform(post("/management/clear-caches"))
                .andExpect(status().isNoContent());

        verify(mockCache, times(1)).clear();
    }

    @Test
    void shouldReturnServiceInfo() throws Exception {
        mockMvc.perform(get("/management/info")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("recommendation-service"))
                .andExpect(jsonPath("$.version").value("0.0.1-SNAPSHOT"));
    }
}
