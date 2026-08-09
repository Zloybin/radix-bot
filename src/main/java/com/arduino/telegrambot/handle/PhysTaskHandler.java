package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.NumberSystem;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.Task;
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
public class PhysTaskHandler implements UpdateHandler{

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
        var user = userService.getUser(userRequest.getChatId());

        int randomTaskId = new Random().nextInt((int) taskRepository.count());

        var physTask = taskRepository.findById((long) randomTaskId).get();

//        Task task;
//        String template;
//        switch (user.getState()) {
//            case UserState.FREE ->  {
//                task = taskService.generateTask();
//                template = "task";
//                user.setState(UserState.TASK);
//                user.setTask(task);
//            }
//            case UserState.TASK -> {
//                task = user.getTask();
//                template = "uncompleted_task";
//            }
//
//            default -> throw new RuntimeException("Неопределнное значение статуса пользователя.");
//        }





        Context context = new Context();

        context.setVariable("title", physTask.getTitle());

        context.setVariable("taskNumber", physTask.getTaskNumber());

        context.setVariable("selfTaskNumber", physTask.getSelfTaskNumber());

        context.setVariable("taskLevel", physTask.getTaskLevel());

        context.setVariable("taskText", physTask.getTaskText());

        context.setVariable("pageNumber", physTask.getPageNumber());

        String text = engine.process("phys_task_message", context);

        var keyboard = keyboardBuilder.buildRadConverterMenu();

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, text, ParseMode.HTML);

    }
}
