package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CancelDeleteCardHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private UserService userService;


    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiService ankiService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "cancelDeleteCard".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var currentCard = ankiService.getCurrentCard().block();
        var deckStats = ankiService.getDeckStats(currentCard.deckName()).block();

        var user = userService.findById(userRequest.getChatId());

        String text;
        InlineKeyboardMarkup keyboard;
        if(user.isAnswerSide()){
            text = templateProcessor.processBackCardTemplate(currentCard, deckStats);
            keyboard = keyboardBuilder.buildAnkiAnswerKeyboard(currentCard.buttons());
        }else{
            text = templateProcessor.processFrontCardTemplate(currentCard, deckStats);
            keyboard = keyboardBuilder.buildAnkiCardKeyboard();
        }

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }
}
