package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.ResultService;
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
public class CorrectingResultTrueHandler implements UpdateHandler {

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
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "changeToTrue".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var physTaskId = user.getPhysTaskId();

        var task = taskService.findById(physTaskId);

        var actualResult = user.getResults().stream()
                .filter(result -> result.getTask().getId().equals(task.getId()))
                .findFirst();

        boolean updatedResultValue = true;
        actualResult.ifPresent(result -> {
            result.setResult(updatedResultValue);
            resultService.save(result);
            System.out.println(String.format("Результат с id: %s был изменен на значение: true", result.getId()));
        });

        user.setPhysTaskId(0);
        userService.save(user);

        var keyboard = keyboardBuilder.buildCompletedPhysTaskMenu();
        var text = templateProcessor.processSuccessCorrectTemplate(task.getId(), updatedResultValue);

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
    }
}
