package com.arduino.telegrambot.ai;

import com.arduino.telegrambot.service.GroqService;
import com.arduino.telegrambot.service.TelegramService;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

@Service
public class GroqLLMService implements LLMService{

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
