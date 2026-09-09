package com.arduino.telegrambot.duocards;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.piper.PiperTtsService;
import com.arduino.telegrambot.rich.TelegramRichMessageService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

        var text = templateProcessor.processBackCardDuoCardsTemplate(currentCard, ankiDeckStats);

        var audio = piperTtsService.synthesize(word).block();

        telegramService.editRichMessageWithAudio(userRequest.getChatId(), userRequest.getMessageId(), keyboardMarkup, text, audio, word);

    }
}
