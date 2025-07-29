package com.bank.star.recommendationservice.controller;

import com.bank.star.recommendationservice.dto.RecommendationDto;
import com.bank.star.recommendationservice.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/recommendation")
@Tag(name = "Recommendation API", description = "API для получения банковских рекомендаций пользователям")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Получить рекомендации для пользователя",
            description = "По переданному userId возвращает список рекомендованных банковских продуктов",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список рекомендаций успешно получен",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = """
                        {
                          "user_id": "d4a4d619-9a0c-4fc5-b0cb-76c49409546b",
                          "recommendations": [
                            {
                              "id": "59efc529-2fff-41af-baff-90ccd7402925",
                              "name": "Top Saving",
                              "text": "Откройте свою собственную «Копилку» с нашим банком!"
                            }
                          ]
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
            }
    )
    public Map<String, Object> getRecommendations(@PathVariable UUID userId) {
        List<RecommendationDto> recommendations = service.getRecommendations(userId);

        return Map.of(
                "user_id", userId,
                "recommendations", recommendations
        );
    }
}
