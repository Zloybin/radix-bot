package com.arduino.telegrambot.feature.ai.groq;

import com.arduino.telegrambot.entity.DuoCardExample;
import com.arduino.telegrambot.feature.ai.groq.model.GroqChatRequest;
import com.arduino.telegrambot.feature.ai.groq.model.GroqChatResponse;
import com.arduino.telegrambot.feature.ai.groq.service.GroqService;
import com.arduino.telegrambot.feature.duocards.model.AnswerCheckResult;
import com.arduino.telegrambot.feature.duocards.model.GeneratedExample;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GroqLLMService implements com.arduino.telegrambot.feature.ai.LLMService {

    @Autowired
    private GroqService groqService;

    @Override
    public String process(String taskText) {
        return groqService.ask(taskText).block();
    }

    @Override
    public String process(String taskText, String userAnswer) {
        return groqService.checkPhysicsSolution(taskText, userAnswer).block();
    }

    @Override
    public DuoCardExample processTranslationMatching(String original) {
        return groqService.matchTranslation(original).block();
    }

    @Override
    public GeneratedExample generateFromCard(String original) {
        return groqService.generateFromCard(original).block();
    }

    @Override
    public AnswerCheckResult checkAnswer(String sourcePhrase, String taskTranslation, String expectedAnswer, String userAnswer) {
        return groqService.checkAnswer(sourcePhrase, taskTranslation, expectedAnswer,userAnswer).block();
    }
}
