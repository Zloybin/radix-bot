package com.arduino.telegrambot.rich;

import lombok.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramRichMessageService {

    private final WebClient webClient;
    private final String botToken;

    public TelegramRichMessageService(
            WebClient.Builder webClientBuilder,
            @Value("${telegram.bot.token}") String botToken
    ) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.telegram.org")
                .build();

        this.botToken = botToken;
    }

    public Mono<String> editRichMessage(
            long chatId,
            long messageId,
            String html,
            InlineKeyboardMarkup keyboard
    ) {
        Map<String, Object> richMessage = Map.of(
                "html", html
        );

        Map<String, Object> request = new HashMap<>();

        request.put("chat_id", chatId);
        request.put("message_id", messageId);
        request.put("rich_message", richMessage);
        request.put("reply_markup", keyboard);

        return webClient.post()
                .uri("/bot{token}/editMessageText", botToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }
}
