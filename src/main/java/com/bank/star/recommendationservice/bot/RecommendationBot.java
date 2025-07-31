package com.bank.star.recommendationservice.bot;

import com.bank.star.recommendationservice.repository.RecommendationsRepository;
import com.bank.star.recommendationservice.service.RecommendationService;
import com.bank.star.recommendationservice.dto.RecommendationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class RecommendationBot extends TelegramLongPollingBot {

    private final RecommendationService recommendationService;
    private final RecommendationsRepository recommendationsRepository;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public RecommendationBot(RecommendationService recommendationService,
                             RecommendationsRepository recommendationsRepository) {
        this.recommendationService = recommendationService;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            if (text.equals("/start")) {
                send(chatId, "Привет! Используй команду /recommend <Имя Фамилия> чтобы получить рекомендации.");
            } else if (text.startsWith("/recommend")) {
                String[] parts = text.split(" ", 2);
                if (parts.length != 2) {
                    send(chatId, "Формат команды: /recommend <Имя Фамилия>");
                    return;
                }

                String fullName = parts[1].trim();
                recommendationsRepository.findUserIdByFullName(fullName).ifPresentOrElse(userId -> {
                    List<RecommendationDto> recs = recommendationService.getRecommendations(userId);
                    if (recs.isEmpty()) {
                        send(chatId, "Пользователь найден, но нет рекомендаций.");
                    } else {
                        StringBuilder sb = new StringBuilder("Здравствуйте ").append(fullName).append("\n");
                        sb.append("Новые продукты для вас:\n");
                        recs.forEach(r -> sb.append("- ").append(r.getName()).append("\n"));
                        send(chatId, sb.toString());
                    }
                }, () -> send(chatId, "Пользователь не найден."));
            }
        }
    }

    private void send(String chatId, String text) {
        try {
            execute(new SendMessage(chatId, text));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
