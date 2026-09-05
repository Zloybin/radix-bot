package com.arduino.telegrambot.ai;

public interface LLMService {
    String process(String taskText);
    String process(String taskText, String userAnswer);
}
