package com.arduino.telegrambot.feature.duocards.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AcceptSentenceExerciseHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "acceptSentenceExercise".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.WAIT_EXERCISE_SENTENCE_ANSWER);
        user.setMessageId(userRequest.getMessageId());
        userService.save(user);

        var keyboard = keyboardBuilder.buildCancelSentenceExerciseKeyboard();

        telegramService.editKeyboard(userRequest.getChatId(), userRequest.getMessageId(),keyboard);

    }
}
