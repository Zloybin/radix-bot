package com.arduino.telegrambot.feature.physik.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.repository.TaskRepository;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AcceptAnswerPhysHandler implements UpdateHandler {

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;


    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "givePhysAnswer".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.WAIT_USER_PHYS_ANSWER);
        userService.save(user);

        var keyboard = keyboardBuilder.buildWaitingForAnswerMenu();
        telegramService.editRichMessageKeyboard(userRequest.getChatId(), userRequest.getMessageId(), keyboard);
        user.setMessageId(userRequest.getMessageId());
        userService.save(user);

    }
}
