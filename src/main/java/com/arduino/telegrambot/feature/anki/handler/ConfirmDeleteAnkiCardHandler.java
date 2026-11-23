package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;

import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;


@Component
public class ConfirmDeleteAnkiCardHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiServiceImpl ankiService;


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "deleteAnkiCard".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
        if (user.isAnswerSide()) {
            user.setAnswerSide(false);
            userService.save(user);
        }

        var currentCard = ankiService.getCurrentCardInfo().block();
        var deckName = currentCard.getDeckName();

        AnkiCurrentCard updatedCurrentCard;

        if(ankiService.deleteCard(currentCard.getCardId()).block()){
            if (ankiService.startDeckReview(deckName).block()){
                    updatedCurrentCard = ankiService.getCurrentCardInfo().block();
            }else {
                throw new AnkiConnectException("Не получилось показать ответ.");
            }

        }else {
            throw new AnkiConnectException("Не получилось заблокировать карточку.");
        }


        var stats = ankiService.getAnkiDecksStatisticsMap(List.of(deckName)).block();
        var ankiDeckStats = stats.get(deckName);


        String text;
        InlineKeyboardMarkup keyboard;
        if(user.isAnswerSide()){
            keyboard = keyboardBuilder.buildAnkiAnswerKeyboard(updatedCurrentCard.getButtons());
            text = templateProcessor.processBackCardTemplate(updatedCurrentCard, ankiDeckStats);
        } else{
            keyboard = keyboardBuilder.buildAnkiCardKeyboard();
            text = templateProcessor.processFrontCardTemplate(updatedCurrentCard, ankiDeckStats);
        }


        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
