package com.arduino.telegrambot.feature.ai.groq.service;

import com.arduino.telegrambot.entity.DuoCardExample;
import com.arduino.telegrambot.feature.anki.ai.prompt.PhysicsPrompt;
import com.arduino.telegrambot.feature.ai.groq.configuration.GroqProperties;
import com.arduino.telegrambot.feature.ai.groq.model.GroqChatRequest;
import com.arduino.telegrambot.feature.ai.groq.model.GroqChatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GroqService {

    private final WebClient groqWebClient;
    private final GroqProperties properties;
    private final PhysicsPrompt physicsPrompt;

    public GroqService(
            WebClient groqWebClient,
            GroqProperties properties,
            PhysicsPrompt physicsPrompt) {
        this.groqWebClient = groqWebClient;
        this.properties = properties;
        this.physicsPrompt = physicsPrompt;
    }

    public Mono<String> ask(String prompt) {

        var request = new GroqChatRequest(
                properties.model(),
                List.of(
                        new GroqChatRequest.Message(
                                "system",
                                physicsPrompt.get()
                        ),
                        new GroqChatRequest.Message(
                                "user",
                                prompt
                        )
                )
        );

        return groqWebClient
                .post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GroqChatResponse.class)
                .map(response ->
                        response.choices()
                                .getFirst()
                                .message()
                                .content()
                );
    }

    public Mono<String> checkPhysicsSolution(
            String taskText,
            String userAnswer
    ) {

        String userMessage = """
                Условие задачи:
                %s

                Решение пользователя:
                %s
                """.formatted(taskText, userAnswer);

        var request = new GroqChatRequest(
                properties.model(),
                List.of(
                        new GroqChatRequest.Message(
                                "system",
                                physicsPrompt.get()
                        ),
                        new GroqChatRequest.Message(
                                "user",
                                userMessage
                        )
                )
        );

        return groqWebClient
                .post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GroqChatResponse.class)
                .map(response ->
                        response.choices()
                                .getFirst()
                                .message()
                                .content()
                );
    }

    public Mono<DuoCardExample> matchTranslation(String original) {
        String userMessage = """
                Словосочетание на немецком:
                %s
                """.formatted(original);

        var request = new GroqChatRequest(
                properties.model(),
                List.of(
                        new GroqChatRequest.Message(
                                "system",
                                physicsPrompt.getDuoCardsPrompt()
                        ),
                        new GroqChatRequest.Message(
                                "user",
                                userMessage
                        )
                )
        );

        return groqWebClient
                .post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GroqChatResponse.class)
                .doOnNext(response -> System.out.println(response))
                .map(response ->
                        response.choices()
                                .getFirst()
                                .message()
                                .content()
                )
                .map(this::parseCard);
    }

    private DuoCardExample parseCard(String raw) {
        var objectMapper = new ObjectMapper();
        String cleaned = raw.trim();
        // на случай если модель всё же обернёт в ```json ... ```
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("^```(json)?", "").replaceAll("```$", "").trim();
        }
        try {
            return objectMapper.readValue(cleaned, DuoCardExample.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Не удалось распарсить ответ модели: " + raw, e);
        }
    }
}
