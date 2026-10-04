package com.arduino.telegrambot.feature.ai.groq.prompt;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class PhysicsPrompt {

    private final String prompt;
    private final String duoCardsPrompt;
    private final String exampleGenerationPrompt;
    private final String answerCheckPrompt;

    public PhysicsPrompt() throws IOException {
        var resource = new ClassPathResource(
                "prompts/physics-checker.txt"
        );

        this.prompt = resource.getContentAsString(
                StandardCharsets.UTF_8
        );

        var duoResource = new ClassPathResource(
                "prompts/german-example-matcher.txt"
        );

        this.duoCardsPrompt = duoResource.getContentAsString(
                StandardCharsets.UTF_8
        );

        var exampleGenerationResource = new ClassPathResource(
                "prompts/example-generation.txt"
        );

        this.exampleGenerationPrompt = exampleGenerationResource.getContentAsString(
                StandardCharsets.UTF_8
        );

        var answerCheckPrompt = new ClassPathResource(
                "prompts/answer-check.txt"
        );

        this.answerCheckPrompt = answerCheckPrompt.getContentAsString(
                StandardCharsets.UTF_8
        );

    }

    public String get() {
        return prompt;
    }

    public String getDuoCardsPrompt() {
        return duoCardsPrompt;
    }

    public String getExampleGenerationPrompt() {
        return exampleGenerationPrompt;
    }

    public String getAnswerCheckPrompt() {
        return answerCheckPrompt;
    }
}
