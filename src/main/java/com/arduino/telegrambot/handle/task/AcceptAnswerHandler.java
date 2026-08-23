package com.arduino.telegrambot.handle.task;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.NumberSystem;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

@Component
public class AcceptAnswerHandler implements UpdateHandler {

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "giveAnswer".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.WAIT_USER_RAD_ANSWER);
        userService.save(user);
        String task = user.getTask();

        if (task == null) {
            throw new RuntimeException("radTask must be != null and not ''.");
        }

        var keyboard = keyboardBuilder.buildBackToRadConverterMenu();

        telegramService.editKeyboard(userRequest.getChatId(), userRequest.getMessageId(), keyboard);

    }
}
