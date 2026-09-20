package com.arduino.telegrambot.feature.duocards.handler;

import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.feature.piper.PiperTtsService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

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

    @Autowired
    private PiperTtsService piperTtsService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "startDuoCards".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        AnkiCurrentCard currentCard;
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
            text = templateProcessor.processFrontCardDuoCardsTemplate(currentCard, deckStats);
            var word = currentCard.question();
            keyboard = keyboardBuilder.buildAnkiShowAnswerDuoCardsKeyboard(word);
//            var audio = piperTtsService.synthesize(currentCard.question()).block();
            telegramService
                    .editRichMessageWithAudio(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text, null, word);
        }else{
            throw new AnkiConnectException("не получилось запустить Review режим в колоде Deutsch.");
        }

    }
}
