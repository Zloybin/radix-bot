package com.arduino.telegrambot.feature.ai.gemini;

import com.arduino.telegrambot.entity.DuoCardExample;
import com.arduino.telegrambot.feature.duocards.model.AnswerCheckResult;
import com.arduino.telegrambot.feature.duocards.model.GeneratedExample;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class LLMGeminiService implements com.arduino.telegrambot.feature.ai.LLMService {

//    @Autowired
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

    @Override
    public String process(String taskText, String userAnswer) {
        return "";
    }

    @Override
    public DuoCardExample processTranslationMatching(String original) {
        return null;
    }

    @Override
    public GeneratedExample generateFromCard(String originalSentence) {
        return null;
    }

    @Override
    public AnswerCheckResult checkAnswer(String sourcePhrase, String taskTranslation, String expectedAnswer, String userAnswer) {
        return null;
    }
}
