package com.arduino.telegrambot.ai;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class LLMService {

    @Autowired
    private Client client;

    public String process(String taskText){

        String systemPrompt;

        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("prompts/physics-checker.txt")) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Файл prompts/physics-checker.txt не найден"
                );
            }

            systemPrompt = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }catch (IOException e){
            throw new IllegalStateException(e.getCause());
        }


        GenerateContentConfig config = GenerateContentConfig.builder()
                .systemInstruction(
                        Content.fromParts(
                                Part.fromText(systemPrompt)
                        )
                )
                .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.7-flash",
                        taskText,
                        config
                );

        return response.text();
    }
}
