package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import reactor.core.publisher.Mono;

@Component
public class DeleteCardHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;

    @Autowired
    private AnkiService ankiService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "confirmDeleteAnkiCard".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var currentCard = ankiService.getCurrentCard().block();
        if(currentCard == null){
            throw new AnkiConnectException("Не получилось получить текущую карточку.");
        }
        var keyboard = keyboardBuilder.buildConfirmDeleteCardKeyboard();

        var deckStats = ankiService.getDeckStats(currentCard.deckName()).block();
        var text = templateProcessor.processConfirmDeleteCardTemplate(currentCard, deckStats);
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
