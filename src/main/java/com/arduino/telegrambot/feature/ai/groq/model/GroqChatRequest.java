package com.arduino.telegrambot.feature.ai.groq.model;

import java.util.List;

public record GroqChatRequest(
        String model,
        List<Message> messages
) {

    public record Message(
            String role,
            String content
    ) {
    }
}
