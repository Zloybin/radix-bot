package com.arduino.telegrambot.anki.handler;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;

import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

import java.util.List;


@Component
public class DeleteAnkiCardHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiService ankiService;


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "deleteAnkiCard".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var currentCard = ankiService.getCurrentCard().block();
        var deckName = currentCard.deckName();

        AnkiCurrentCard updatedCurrentCard;

        if(ankiService.deleteCard(currentCard.cardId()).block()){
            if (ankiService.startStudy(deckName).block()){
                    updatedCurrentCard = ankiService.getCurrentCard().block();
            }else {
                throw new AnkiConnectException("Не получилось показать ответ.");
            }

        }else {
            throw new AnkiConnectException("Не получилось заблокировать карточку.");
        }


        var stats = ankiService.getDecksStats(List.of(deckName)).block();
        var ankiDeckStats = stats.get(deckName);


        var keyboard = keyboardBuilder.buildAnkiShowAnswerKeyboard();
        var text = templateProcessor.processFrontCardTemplate(updatedCurrentCard, ankiDeckStats);

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
    }
}
