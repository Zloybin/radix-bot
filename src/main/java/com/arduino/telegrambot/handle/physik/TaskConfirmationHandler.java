package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class TaskConfirmationHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateEngine engine;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "confirmTask".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());

        user.setPhysTaskId(0);
        userService.save(user);

        var context = new Context();

        var message = engine.process("confirm_phys_task_complete", context);
        var keyboard = keyboardBuilder.buildCompletedPhysTaskMenu();
        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), message, keyboard, ParseMode.HTML);
    }
}
