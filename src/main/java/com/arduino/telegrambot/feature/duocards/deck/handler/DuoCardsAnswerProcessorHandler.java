package com.arduino.telegrambot.feature.duocards.deck.handler;

import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.feature.anki.enummeration.AnkiAnswer;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.feature.piper.PiperTtsService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class DuoCardsAnswerProcessorHandler implements UpdateHandler {

    public static final int COUNT_NULL = 0;
    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiServiceImpl ankiService;

    @Autowired
    private PiperTtsService piperTtsService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "duoCardsAnswer".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        AnkiCurrentCard currentCard;

        if (ankiService.showAnswer().block()) {
            currentCard = ankiService.getCurrentCardInfo().block();
        } else {
            throw new AnkiConnectException("Не смог открыть ответ карточки, т. к. не включен режим Review");
        }

        var deckName = currentCard.getDeckName();

        String text;
        InlineKeyboardMarkup keyboard;
        AnkiCurrentCard updatedCurrentCard;

        int userAnswerIndex = getUserAnswerIndex(userRequest);

        if (Boolean.FALSE.equals(ankiService.answerCard(userAnswerIndex).block())) {
            throw new AnkiConnectException("Не получилось обработать ответ пользователя.");
        }

        var deckStats = ankiService.getAnkiDeckStatistics(deckName).block();

        if (isAllCountsNull(deckStats)) {

            text = templateProcessor.processCompletedDeckTemplate(deckName);
            keyboard = keyboardBuilder.buildBackToDuoCardsMenuKeyboard();

        } else {

            updatedCurrentCard = ankiService.getCurrentCardInfo().block();
            text = templateProcessor.processFrontCardDuoCardsTemplate(updatedCurrentCard, deckStats);

            var audio = piperTtsService.synthesize(updatedCurrentCard.getQuestion()).block();


            //Youglish button added in keyboard
            String word = currentCard.getQuestion();
            keyboard = keyboardBuilder.buildAnkiShowAnswerDuoCardsKeyboard(word);
            telegramService.editRichMessageWithAudio(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text, audio, word);
            return;
        }

        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        userService.save(user);

        telegramService.editRichMessage(userRequest.getChatId(),userRequest.getMessageId(), keyboard, text);

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

    private static boolean isAllCountsNull(AnkiDeckStats ankiDeckStats) {
        return ankiDeckStats.getNewCount() == COUNT_NULL && ankiDeckStats.getLearnCount() == COUNT_NULL && ankiDeckStats.getReviewCount() == COUNT_NULL;
    }
}
