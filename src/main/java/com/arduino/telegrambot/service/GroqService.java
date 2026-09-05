package com.arduino.telegrambot.service;

import com.arduino.telegrambot.ai.prompt.PhysicsPrompt;
import com.arduino.telegrambot.config.GroqProperties;
import com.arduino.telegrambot.model.GroqChatRequest;
import com.arduino.telegrambot.model.GroqChatResponse;
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
}
