package com.arduino.telegrambot.feature.physik.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CancelPhysTaskHandler implements UpdateHandler {

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
        return "cancelPhysTask".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        long randomTaskId;
        var user = userService.findById(userRequest.getChatId());

        if (user.isExcluded()) {
            List<Long> completedTaskIds = new ArrayList<>();
            user.getResults().stream().map(result -> result.getTask().getId()).distinct().forEach(completedTaskIds::add);
            randomTaskId = taskService.getRandomPhysTaskId(completedTaskIds);

        } else {
            randomTaskId = taskService.getRandomPhysTaskId();

        }
        user.setPhysTaskId(randomTaskId);
        userService.save(user);

        var physTask = taskService.findById(randomTaskId);

        var section = physTask.getSection().getRussianName();
        var title = physTask.getTitle();
        var taskNumber = physTask.getTaskNumber();
        var selfTaskNumber = physTask.getSelfTaskNumber();
        var taskLevel = physTask.getTaskLevel().getTitle();
        var taskText = physTask.getTaskText();
        var pageNumber = physTask.getPageNumber();

        var text = templateProcessor.processPhysTaskTemplate(section, title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber);

        var keyboard = keyboardBuilder.buildPhysTaskCardMenu();

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
