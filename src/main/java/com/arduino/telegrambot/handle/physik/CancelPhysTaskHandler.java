package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.Task;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.repository.TaskRepository;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class CancelPhysTaskHandler implements UpdateHandler {

    @Autowired
    private TaskRepository taskRepository;

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
        return "cancelPhysTask".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        long randomTaskId;
        var user = userService.findById(userRequest.getChatId());
        userService.save(user);

        if (user.isExcluded()) {
            List<Long> completedTaskIds = new ArrayList<>();
            user.getResults().stream().map(result -> result.getTask().getId()).distinct().forEach(completedTaskIds::add);
            randomTaskId = taskService.getRandomPhysTaskId(completedTaskIds);
            user.setPhysTaskId(randomTaskId);
        } else {
            randomTaskId = taskService.getRandomPhysTaskId();
            user.setPhysTaskId(randomTaskId);
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
