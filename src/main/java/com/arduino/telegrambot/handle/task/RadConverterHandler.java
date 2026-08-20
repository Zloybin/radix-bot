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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class RadConverterHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "radConverter".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        String task = user.getTask();

        String source = NumberSystem.valueOf(task.substring(0, 3)).getTitle();
        String target = NumberSystem.valueOf(task.substring(3, 6)).getTitle();
        String taskNumber = task.substring(6);

        var text = templateProcessor.processRadTaskTemplate(source, target, taskNumber);
        var keyboard = keyboardBuilder.buildRadConverterMenu();

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, text, ParseMode.HTML);
    }
}
