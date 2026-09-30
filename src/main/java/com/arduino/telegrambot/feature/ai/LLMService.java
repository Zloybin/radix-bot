package com.arduino.telegrambot.feature.ai;

import com.arduino.telegrambot.entity.DuoCardExample;

public interface LLMService {
    String process(String taskText);
    String process(String taskText, String userAnswer);
    DuoCardExample processTranslationMatching(String original);
}
