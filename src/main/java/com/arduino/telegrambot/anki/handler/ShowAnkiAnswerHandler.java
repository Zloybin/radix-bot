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

import java.util.List;

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

        AnkiCurrentCard currentCard;

        if(ankiService.showAnswer().block()){
            currentCard = ankiService.getCurrentCard().block();
        }else{
            throw new AnkiConnectException("Не получилось открыть ответ карточки.");
        }

        List<Integer> buttons = currentCard.buttons();
        var keyboardMarkup = keyboardBuilder.buildAnkiAnswerKeyboard(buttons);

        var text = templateProcessor.processBackCardTemplate(currentCard);

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboardMarkup, ParseMode.HTML);

    }
}
