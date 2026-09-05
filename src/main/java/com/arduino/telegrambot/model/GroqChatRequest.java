package com.arduino.telegrambot.model;

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
