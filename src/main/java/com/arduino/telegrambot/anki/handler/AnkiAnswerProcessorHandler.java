package com.arduino.telegrambot.anki.handler;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
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

@Component
public class AnkiAnswerProcessorHandler implements UpdateHandler {

    public static final int COUNT_NULL = 0;
    public static final String DEUTSCH = "Deutsch";
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
        return "ankiAnswer".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

//        System.out.println("DEUTSCH " +ankiService.startStudy(DEUTSCH).block());
//        var currentCardtest = ankiService.getCurrentCard().block();
//        System.out.println("SHOW ANSWER: " + ankiService.showAnswer().block());

        AnkiCurrentCard currentCard;

        if(ankiService.showAnswer().block()){
            currentCard = ankiService.getCurrentCard().block();
        } else {
            throw new AnkiConnectException("Не смог открыть ответ карточки, т. к. не включен режим Review");
        }

        var deckName = currentCard.deckName();

        String text;
        InlineKeyboardMarkup keyboard;
        AnkiCurrentCard updatedCurrentCard;

        int userAnswerIndex = getUserAnswerIndex(userRequest);

        if (Boolean.FALSE.equals(ankiService.answerCard(userAnswerIndex).block())) {
            throw new AnkiConnectException("Не получилось обработать ответ пользователя.");
        }

        var deckStats = ankiService.getDeckStats(deckName).block();

        if (isAllCountsNull(deckStats)) {

            text = templateProcessor.processCompletedDeckTemplate(deckName);

            if (DEUTSCH.equals(deckName)) {
                keyboard = keyboardBuilder.buildBackToDuoCardsMenuKeyboard();
            } else {
                keyboard = keyboardBuilder.buildBackToAnkiDecksMenu();
            }

        } else {

            updatedCurrentCard = ankiService.getCurrentCard().block();
            text = templateProcessor.processFrontCardTemplate(updatedCurrentCard, deckStats);

            if (DEUTSCH.equals(deckName)) {

                //Youglish button added in keyboard
                keyboard = keyboardBuilder.buildAnkiShowAnswerDuoCardsKeyboard(currentCard.question());

            } else {
                keyboard = keyboardBuilder.buildAnkiShowAnswerKeyboard();
            }
        }

        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        userService.save(user);

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);

    }

    private int getUserAnswerIndex(UserRequest userRequest) {
        int userAnswerIndex = 0;

        for (AnkiAnswer ankiAnswer : AnkiAnswer.values()) {
            if (ankiAnswer.getIndex() == Integer.parseInt(userRequest.getRequest())) {
                userAnswerIndex = ankiAnswer.getIndex();
            }
        }
        return userAnswerIndex;
    }

    private static boolean isAllCountsNull(AnkiDeckStats deckStats) {
        return deckStats.newCount() == COUNT_NULL && deckStats.learnCount() == COUNT_NULL && deckStats.reviewCount() == COUNT_NULL;
    }
}
