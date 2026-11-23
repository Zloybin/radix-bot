package com.arduino.telegrambot.feature.rad.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.feature.rad.enummeration.NumberSystem;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.feature.rad.service.RadTaskService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    private RadTaskService radTaskService;


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "radConverter".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());

        String task = user.getRadTask();

        if (task == null) {
            throw new RuntimeException("radTask = null");
        }

        String taskValue;

        if(task.equals("")) {
            taskValue = radTaskService.generateTask();
            user.setRadTask(taskValue);
            userService.save(user);
        }else{
            taskValue = task;
        }

        String source = NumberSystem.valueOf(taskValue.substring(0, 3)).getDativ();
        String target = NumberSystem.valueOf(taskValue.substring(3, 6)).getAkusativ();
        String taskNumber = taskValue.substring(6);

        var text = templateProcessor.processRadTaskTemplate(source, target, taskNumber);
        var keyboard = keyboardBuilder.buildRadConverterTaskMenu();

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
