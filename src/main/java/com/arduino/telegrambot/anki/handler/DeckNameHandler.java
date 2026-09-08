package com.arduino.telegrambot.anki.handler;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
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

@Component
public class DeckNameHandler implements UpdateHandler {

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
        UserState state = user.getState();
        return UserState.WAIT_DECK_NAME.equals(state);
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        userService.save(user);

        //        System.out.println("DEUTSCH " +ankiService.startStudy(DEUTSCH).block());
        //        var currentCardtest = ankiService.getCurrentCard().block();
        //        System.out.println("SHOW ANSWER: " + ankiService.showAnswer().block());

        AnkiCurrentCard currentCard;
        InlineKeyboardMarkup keyboard;
        String text;

        String requestedDeckName = userRequest.getRequest();
        System.out.println(String.format("Включаем Review для колоды: %s.", requestedDeckName));

        try {

            if (ankiService.startStudy(requestedDeckName).block()) {
                currentCard = ankiService.getCurrentCard().block();
            } else {
                throw new AnkiConnectException(String.format("Не получилось открыть колоду: %s", requestedDeckName));
            }

        } catch (AnkiConnectException e) {
            if ("Gui review is not currently active.".equals(e.getMessage())) {

                text = templateProcessor.processCompletedDeckTemplate(requestedDeckName);

                if ("Deutsch".equals(requestedDeckName)) {
                    keyboard = keyboardBuilder.buildBackToDuoCardsMenuKeyboard();
                } else {
                    keyboard = keyboardBuilder.buildBackToAnkiDecksMenu();
                }

                telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
                return;

            } else {
                throw new AnkiConnectException(e.getMessage());
            }
        }

        var deckStats = ankiService.getDeckStats(currentCard.deckName()).block();
        text = templateProcessor.processFrontCardTemplate(currentCard, deckStats);
        keyboard = keyboardBuilder.buildAnkiShowAnswerKeyboard();

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
