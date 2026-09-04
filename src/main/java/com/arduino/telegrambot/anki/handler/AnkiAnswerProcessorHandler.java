package com.arduino.telegrambot.anki.handler;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.AnkiAnswer;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class AnkiAnswerProcessorHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

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
        var user = userService.findById(userRequest.getChatId());

        return UserState.WAIT_ANKI_ANSWER.equals(user.getState());
    }

    @Override
    public void handle(UserRequest userRequest) {

        AnkiCurrentCard currentCard = null;
        for (AnkiAnswer ankiAnswer : AnkiAnswer.values()) {
            if(ankiAnswer.getIndex() == Integer.parseInt(userRequest.getRequest())){
                currentCard = ankiService.answerAndGetNextCard(ankiAnswer.getIndex()).block();
                break;
            }
        }

        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        userService.save(user);

        var deckName = currentCard.deckName();

        Map<String, AnkiDeckStats> deckStats = ankiService.getDeckStats(List.of(deckName)).block();
        AnkiDeckStats ankiDeckStats = deckStats.get(deckName);

        InlineKeyboardMarkup keyboard;

        if("Deutsch".equals(deckName)){
            keyboard = keyboardBuilder.buildAnkiShowAnswerDuoCardsKeyboard(deckName);
        }else{
            keyboard = keyboardBuilder.buildAnkiShowAnswerKeyboard();
        }

        var text = templateProcessor.processFrontCardTemplate(currentCard, ankiDeckStats);


        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);

    }
}
