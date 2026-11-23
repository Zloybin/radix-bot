package com.arduino.telegrambot.feature.ai;

import com.arduino.telegrambot.feature.duocards.web.entity.DuoCardExample;
import com.arduino.telegrambot.feature.duocards.sentence.model.AnswerCheckResult;
import com.arduino.telegrambot.feature.duocards.sentence.model.GeneratedExample;

public interface LLMService {
    String process(String taskText);
    String process(String taskText, String userAnswer);
    DuoCardExample processTranslationMatching(String original);
    GeneratedExample generateFromCard(String originalSentence);
    AnswerCheckResult checkAnswer(String sourcePhrase, String taskTranslation, String expectedAnswer, String userAnswer);
}
