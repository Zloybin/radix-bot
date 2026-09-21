package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;

import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ConfirmDeleteAnkiCardHandler implements UpdateHandler {

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
        return "deleteAnkiCard".equals(userRequest.getHandler());
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

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
