package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
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
public class PhysTaskHandler implements UpdateHandler {

    private final String handlerCallback = "physTask";

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return handlerCallback.equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        long randomTaskId;

        var user = userService.findById(userRequest.getChatId());

        if (user.getPhysTaskId() == 0) {

            randomTaskId = taskService.getRandomPhysTaskId();
            user.setPhysTaskId(randomTaskId);
            userService.save(user);

        } else {
            randomTaskId = user.getPhysTaskId();
        }

        var physTask = taskService.findById(randomTaskId);

        var title = physTask.getTitle();
        var taskNumber = physTask.getTaskNumber();
        var selfTaskNumber = physTask.getSelfTaskNumber();
        var taskLevel = physTask.getTaskLevel().getTitle();
        var taskText = physTask.getTaskText();
        var pageNumber = physTask.getPageNumber();

        var text = templateProcessor.processPhysTaskTemplate(title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber);

        var keyboard = keyboardBuilder.buildPhysTaskMenu();

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);

    }
}
