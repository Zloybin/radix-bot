package com.arduino.telegrambot.feature.physik.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelPhysAnswerHandler implements UpdateHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TaskService taskService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "cancelPhysAnswer".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        userService.save(user);

        var keyboard = keyboardBuilder.buildPhysTaskCardMenu();
        telegramService.editRichMessageKeyboard(userRequest.getChatId(), userRequest.getMessageId(), keyboard);

    }
}
