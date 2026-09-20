package com.arduino.telegrambot.feature.ai;

public interface LLMService {
    String process(String taskText);
    String process(String taskText, String userAnswer);
}
