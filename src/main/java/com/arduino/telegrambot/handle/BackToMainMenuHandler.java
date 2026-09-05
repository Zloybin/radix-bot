package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class BackToMainMenuHandler implements UpdateHandler{

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "backToMainMenu".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var chatId = userRequest.getChatId();
        var user = userService.findById(chatId);
        user.setState(UserState.FREE);
        userService.save(user);

        var keyboard = keyboardBuilder.buildMainMenu();
        var text = templateProcessor.processGreetingsTemplate();

        telegramService.editMessage(chatId, userRequest.getMessageId(), text, keyboard, ParseMode.HTML);

    }
}
