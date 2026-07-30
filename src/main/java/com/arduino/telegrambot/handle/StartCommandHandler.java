package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.thymeleaf.context.Context;
import org.thymeleaf.TemplateEngine;

@Component
public class StartCommandHandler implements UpdateHandler {
    private final String handlerCallback = "/start";

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;



    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return handlerCallback.equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        String test = engine.process("greetings", new Context());

        var keyboard = keyboardBuilder.buildMainMenu();

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, test, ParseMode.HTML);
    }
}
