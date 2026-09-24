package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class MarkAnkiCardHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiServiceImpl ankiService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "markAnkiCard".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var currentCard = ankiService.getCurrentCard().block();
        var cardId = currentCard.cardId();
        AnkiCurrentCard updatedCurrentCard;

        if(ankiService.markCard(cardId).block()){
            updatedCurrentCard = ankiService.getCurrentCard().block();
        }else{
            throw new AnkiConnectException("Не удалось отметить карту флагом.");
        }

        var deckStats = ankiService.getDeckStats(updatedCurrentCard.deckName()).block();

        var keyboard = keyboardBuilder.buildAnkiCardKeyboard();
        var text = templateProcessor.processFrontCardTemplate(currentCard, deckStats);

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);


    }
}
