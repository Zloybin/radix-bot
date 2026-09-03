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

        AnkiCurrentCard currentCard = null;

        if(ankiService.startStudy(userRequest.getRequest()).block()){
            currentCard = ankiService.getCurrentCard().block();
        }else{
            throw new AnkiConnectException("Не получилось запустить режим Review.");
        }

        var text = templateProcessor.processFrontCardTemplate(currentCard);
        var keyboard = keyboardBuilder.buildAnkiShowAnswerKeyboard();

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);


    }
}
