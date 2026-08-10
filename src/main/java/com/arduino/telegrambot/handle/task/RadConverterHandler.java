package com.arduino.telegrambot.handle.task;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.NumberSystem;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class RadConverterHandler implements UpdateHandler {

    private final String handlerCallback = "radConverter";

    @Autowired
    private TaskService taskService;

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
        return handlerCallback.equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());

        String task;
        String template;
        switch (user.getState()) {
            case UserState.FREE ->  {
                task = taskService.generateTask();
                template = "task";
                user.setState(UserState.TASK);
                user.setTask(task);
            }
            case UserState.TASK -> {
                task = user.getTask();
                template = "uncompleted_task";
            }

            default -> throw new RuntimeException("Неопределнное значение статуса пользователя.");
        }



        Context context = new Context();

        context.setVariable("source", NumberSystem.valueOf(task.substring(0, 3)).getTitle());
        context.setVariable("drain", NumberSystem.valueOf(task.substring(3, 6)).getTitle());
        context.setVariable("task", task.substring(6));

        String text = engine.process(template, context);

        var keyboard = keyboardBuilder.buildRadConverterMenu();

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, text, ParseMode.HTML);
    }
}
