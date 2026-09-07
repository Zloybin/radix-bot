package com.arduino.telegrambot.duocards;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Component
public class StartDuoCardsHandler implements UpdateHandler {

    public static final String DEUTSCH = "Deutsch";
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
        return "startDuoCards".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        AnkiCurrentCard currentCard = null;
        InlineKeyboardMarkup keyboard;
        String text;

        if (ankiService.startStudy(DEUTSCH).block()){
            try {
                currentCard = ankiService.getCurrentCard().block();
            }catch (AnkiConnectException ex){
                text = templateProcessor.processCompletedDeckTemplate(DEUTSCH);
                keyboard = keyboardBuilder.buildBackToDuoCardsMenuKeyboard();
                telegramService
                        .editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
                return;

            }

            var deckStats = ankiService.getDeckStats(DEUTSCH).block();
            text = templateProcessor.processFrontCardTemplate(currentCard, deckStats);
            var question = currentCard.question();
            keyboard = keyboardBuilder.buildAnkiShowAnswerDuoCardsKeyboard(question);
            telegramService
                    .editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
        }else{
            throw new AnkiConnectException("не получилось запустить Review режим в колоде Deutsch.");
        }

    }
}
