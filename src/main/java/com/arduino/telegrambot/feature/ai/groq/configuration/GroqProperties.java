package com.arduino.telegrambot.feature.ai.groq.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "groq")
public record GroqProperties(
        String apiKey,
        String baseUrl,
        String model
) {
}
