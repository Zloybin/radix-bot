package com.arduino.telegrambot.handle.anki;

import com.arduino.telegrambot.anki.AnkiCurrentCard;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
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

        var isStarted = ankiService.startStudy(userRequest.getRequest()).block();
        var isShowQuestion = ankiService.showQuestion().block();
        var currentCard = ankiService.getCurrentCard().block();

        var buttons = currentCard.buttons();
        var keyboard = keyboardBuilder.buildAnkkiAnswerKeyboard(buttons);

        var text = templateProcessor.processFrontCardTemplate(currentCard);


        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);


    }
}
