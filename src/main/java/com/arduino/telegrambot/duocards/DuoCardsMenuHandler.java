package com.arduino.telegrambot.duocards;

import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

@Component
public class DuoCardsMenuHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private AnkiService ankiService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "duoCards".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var text = templateProcessor.processAnkiUserProfileTemplate();
        var keyboard = keyboardBuilder.buildDuoCardsMenuKeyboard();
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
