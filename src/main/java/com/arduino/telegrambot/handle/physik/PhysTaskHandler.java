package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.repository.TaskRepository;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Random;

@Component
public class PhysTaskHandler implements UpdateHandler {

    private final String handlerCallback = "physTask";

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return handlerCallback.equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        int randomTaskId;
        var user = userService.getUser(userRequest.getChatId());
        if(user.getPhysTask() == 0){
            randomTaskId = new Random().nextInt((int) taskRepository.count());
        }else{
            randomTaskId = user.getPhysTask();
        }

        user.setPhysTask(randomTaskId);
        userService.putUser(user.getId(), user);

        var physTask = taskRepository.findById((long) randomTaskId).get();

        Context context = new Context();
        context.setVariable("title", physTask.getTitle());
        context.setVariable("taskNumber", physTask.getTaskNumber());
        context.setVariable("selfTaskNumber", physTask.getSelfTaskNumber());
        context.setVariable("taskLevel", physTask.getTaskLevel());
        context.setVariable("taskText", physTask.getTaskText());
        context.setVariable("pageNumber", physTask.getPageNumber());

        String text = engine.process("phys_task_message", context);

        var keyboard = keyboardBuilder.buildPhysTaskMenu();

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, text, ParseMode.HTML);

    }
}
