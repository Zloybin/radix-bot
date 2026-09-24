package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShowAnkiAnswerHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnkiServiceImpl ankiService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "showAnkiAnswer".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        AnkiCurrentCard currentCard;

        if (ankiService.showAnswer().block()) {
            currentCard = ankiService.getCurrentCard().block();
        } else {
            throw new AnkiConnectException("Не получилось открыть ответ карточки.");
        }

        List<Integer> buttons = currentCard.buttons();

        var deckName = currentCard.deckName();

        var keyboardMarkup = keyboardBuilder.buildAnkiAnswerKeyboard(buttons);

        Map<String, AnkiDeckStats> deckStats = ankiService.getDecksStats(List.of(deckName)).block();
        AnkiDeckStats ankiDeckStats = deckStats.get(deckName);

        var text = templateProcessor.processBackCardTemplate(currentCard, ankiDeckStats);

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboardMarkup, text);

    }
}
