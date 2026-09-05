package com.arduino.telegrambot.ai.prompt;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class PhysicsPrompt {

    private final String prompt;

    public PhysicsPrompt() throws IOException {
        var resource = new ClassPathResource(
                "prompts/physics-checker.txt"
        );

        this.prompt = resource.getContentAsString(
                StandardCharsets.UTF_8
        );
    }

    public String get() {
        return prompt;
    }
}
