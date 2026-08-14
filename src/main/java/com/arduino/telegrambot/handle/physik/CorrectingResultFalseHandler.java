package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.Result;
import com.arduino.telegrambot.entity.Task;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.ResultService;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Optional;

@Component
public class CorrectingResultFalseHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateEngine engine;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "changeToTrue".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var physTask = user.getPhysTask();
        var task = taskService.findById((long) physTask);
        var results = user.getResults();
        var actualResult = results.stream()
                .filter(result -> result.getTask().getId().equals(task.getId()))
                .findFirst();
        actualResult.ifPresent(result -> {
            result.setResult(true);
            resultService.save(result);
            System.out.println(String.format("Результат с id: %s был изменен на значение: true", result.getId()));
        });

        var keyboard = keyboardBuilder.buildCompletedPhysTaskMenu();
        var context = new Context();
        context.setVariable("id", task.getId());
        String message = engine.process("success_correct", context);

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, message, ParseMode.HTML);
    }
}
