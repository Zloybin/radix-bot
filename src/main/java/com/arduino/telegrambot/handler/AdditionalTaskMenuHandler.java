package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdditionalTaskMenuHandler implements UpdateHandler{

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "additionalTask".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var keyboard = keyboardBuilder.buildAdditionalTaskMenuKeyboard();
        telegramService.editRichMessageKeyboard(userRequest.getChatId(), userRequest.getMessageId(), keyboard);
    }
}
