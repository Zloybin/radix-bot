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

@Component
public class ShowAnkiAnswerHandler implements UpdateHandler {

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
        return "showAnkiAnswer".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.WAIT_ANKI_ANSWER);
        userService.save(user);
        var currentCard = ankiService.getCurrentCard().block();

        var text = templateProcessor.processBackCardTemplate(currentCard);

        var keyboardMarkup = keyboardBuilder.buildAnkiAnswerKeyboard(currentCard.buttons());
        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboardMarkup, ParseMode.HTML);

    }
}
