package com.arduino.telegrambot.feature.duocards.handler;

import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.feature.piper.PiperTtsService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ShowDuoCardAnswerHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private AnkiService ankiService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private PiperTtsService piperTtsService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "showAnswerDuocards".equals(userRequest.getHandler());
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

        String word = currentCard.question();
        var keyboardMarkup = keyboardBuilder.buildAnkiAnswerDuoCardsKeyboard(buttons, word);

        Map<String, AnkiDeckStats> deckStats = ankiService.getDecksStats(List.of(deckName)).block();
        AnkiDeckStats ankiDeckStats = deckStats.get(deckName);

        var video = ankiService.getVideo();

        var text = templateProcessor.processBackCardDuoCardsTemplate(currentCard, ankiDeckStats);

//        var audio = piperTtsService.synthesize(word).block();

        telegramService.editRichMessageWithAudioAndVideo(userRequest.getChatId(), userRequest.getMessageId(), keyboardMarkup, text, null, video, word);

    }
}
