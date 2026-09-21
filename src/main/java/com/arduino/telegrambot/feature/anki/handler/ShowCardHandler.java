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
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class ShowCardHandler implements UpdateHandler {

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
        return "showAnkiCard".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        AnkiCurrentCard currentCard;
        InlineKeyboardMarkup keyboard;
        String text;

        String requestedDeckName = userRequest.getRequest();
        System.out.println(String.format("Включаем Review для колоды: %s.", requestedDeckName));

        try {

            if (ankiService.startStudy(requestedDeckName).block()) {
                currentCard = ankiService.getCurrentCard().block();
                System.out.println(String.format("Review-режим для колоды %s включен.", requestedDeckName));
            } else {
                throw new AnkiConnectException(String.format("Не получилось включить Review режим для колоды: %s", requestedDeckName));
            }

        } catch (AnkiConnectException e) {
            if ("Gui review is not currently active.".equals(e.getMessage())) {

                text = templateProcessor.processCompletedDeckTemplate(requestedDeckName);

                if ("Deutsch".equals(requestedDeckName)) {
                    keyboard = keyboardBuilder.buildBackToDuoCardsMenuKeyboard();
                } else {
                    keyboard = keyboardBuilder.buildBackToAnkiDecksMenu();
                }

                telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
                return;

            } else {
                throw new AnkiConnectException(e.getMessage());
            }
        }

        var deckStats = ankiService.getDeckStats(currentCard.deckName()).block();
        text = templateProcessor.processFrontCardTemplate(currentCard, deckStats);
        keyboard = keyboardBuilder.buildAnkiCardKeyboard();

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
