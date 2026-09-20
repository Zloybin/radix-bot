package com.arduino.telegrambot.feature.ai.groq;

import com.arduino.telegrambot.feature.ai.groq.service.GroqService;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

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
}
