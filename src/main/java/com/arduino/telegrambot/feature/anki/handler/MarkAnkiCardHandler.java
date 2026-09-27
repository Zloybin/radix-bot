package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
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
    private UserService userService;

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

        var user = userService.findById(userRequest.getChatId());
        var currentCard = ankiService.getCurrentCard().block();
        var cardId = currentCard.cardId();
        Integer flag = currentCard.flag() == 0 ? 1 : 0;
        AnkiCurrentCard updatedCurrentCard;

        if(ankiService.markCard(cardId, flag).block()){
            updatedCurrentCard = ankiService.getCurrentCard().block();
        }else{
            throw new AnkiConnectException("Не удалось отметить карту флагом.");
        }

        var deckStats = ankiService.getDeckStats(updatedCurrentCard.deckName()).block();




        String text;
        InlineKeyboardMarkup keyboard;
        if (user.isAnswerSide()){
            text = templateProcessor.processBackCardTemplate(updatedCurrentCard, deckStats);
            keyboard = keyboardBuilder.buildAnkiAnswerKeyboard(updatedCurrentCard.buttons());
        }else {
            text = templateProcessor.processFrontCardTemplate(updatedCurrentCard, deckStats);
            keyboard = keyboardBuilder.buildAnkiCardKeyboard();
        }


        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);


    }
}
