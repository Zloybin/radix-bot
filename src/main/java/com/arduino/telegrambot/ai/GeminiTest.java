package com.arduino.telegrambot.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GeminiTest {

    public static void main(String[] args) {

        Client client = Client.builder()
                .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-3.6-flash",
                        "Объясни простыми словами второй закон Ньютона.",
                        null
                );

        System.out.println(response.text());
    }
}
