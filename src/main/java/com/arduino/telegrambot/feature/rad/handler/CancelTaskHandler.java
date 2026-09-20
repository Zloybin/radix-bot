package com.arduino.telegrambot.feature.rad.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.NumberSystem;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CancelTaskHandler implements UpdateHandler {

    @Autowired
    private TaskService taskService;

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
        return "cancelTask".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var task = taskService.generateTask();
        user.setTask(task);
        userService.save(user);

        String source = NumberSystem.valueOf(task.substring(0, 3)).getTitle();
        String target = NumberSystem.valueOf(task.substring(3, 6)).getTitle();
        String taskNumber = task.substring(6);

        var text = templateProcessor.processRadTaskTemplate(source, target, taskNumber);

        var keyboard = keyboardBuilder.buildRadConverterTaskMenu();

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
