package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.repository.TaskRepository;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Component
public class CancelPhysTaskHandler implements UpdateHandler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "cancelPhysTask".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        int randomTaskId = new Random().nextInt((int) taskRepository.count());
        var physTask = taskRepository.findById((long) randomTaskId).get();

        var user = userService.findById(userRequest.getChatId());
        user.setPhysTaskId(randomTaskId);

        Context context = new Context();
        context.setVariable("title", physTask.getTitle());
        context.setVariable("taskNumber", physTask.getTaskNumber());
        context.setVariable("selfTaskNumber", physTask.getSelfTaskNumber());
        context.setVariable("taskLevel", physTask.getTaskLevel());
        context.setVariable("taskText", physTask.getTaskText());
        context.setVariable("pageNumber", physTask.getPageNumber());

        String message = engine.process("phys_task_message", context);

        var keyboard = keyboardBuilder.buildPhysTaskMenu();

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), message, keyboard, ParseMode.HTML);
    }
}
