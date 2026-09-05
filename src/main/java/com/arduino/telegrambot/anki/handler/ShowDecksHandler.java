package com.arduino.telegrambot.anki.handler;

import com.arduino.telegrambot.anki.AnkiService;
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

@Component
public class ShowDecksHandler implements UpdateHandler {

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiService ankiService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "showDecks".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.WAIT_DECK_NAME);
        userService.save(user);

        var text = templateProcessor.processDecksMenuTemplate();

        var decks = ankiService.getDecks().block();
        var keyboard = keyboardBuilder.buildLDecksMenu(decks);

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);

    }
}
