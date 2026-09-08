package com.arduino.telegrambot.anki.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

@Component
public class AnkiMenuHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private UserService userService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "ankiMainMenu".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        userService.save(user);

        var keyboard = keyboardBuilder.buildAnkiMenu();
        var text = templateProcessor.processAnkiUserProfileTemplate();

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);

    }
}
