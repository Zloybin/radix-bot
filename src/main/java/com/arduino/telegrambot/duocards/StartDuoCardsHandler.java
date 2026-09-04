package com.arduino.telegrambot.duocards;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class StartDuoCardsHandler implements UpdateHandler {

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
        if (ankiService.startStudy("Deutsch").block()){
            var currentCard = ankiService.getCurrentCard().block();
            var cardStats = ankiService.getDeckStats(List.of("Deutsch")).block();
            var ankiDeckStats = cardStats.get("Deutsch");
            var text = templateProcessor.processFrontCardTemplate(currentCard, ankiDeckStats);
            var keyboard = keyboardBuilder.buildAnkiAnswerKeyboard(currentCard.buttons());
            telegramService
                    .editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
        }else{
            throw new AnkiConnectException("не получилось запустить Review режим в колоде Deutsch.");
        }

    }
}
