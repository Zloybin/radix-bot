package com.arduino.telegrambot.feature.ai.groq.model;

import java.util.List;

public record GroqChatResponse(
        List<Choice> choices
) {

    public record Choice(
            Message message
    ) {
    }

    public record Message(
            String role,
            String content
    ) {
    }
}
