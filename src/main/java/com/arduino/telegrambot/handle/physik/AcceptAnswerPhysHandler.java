package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
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

@Component
public class AcceptAnswerPhysHandler implements UpdateHandler {

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "givePhysAnswer".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.WAIT_USER_PHYS_ANSWER);
        userService.save(user);
        var taskId = user.getPhysTask();

        var physTask = taskRepository.findById((long) taskId).get();

        Context context = new Context();
        context.setVariable("title", physTask.getTitle());
        context.setVariable("taskNumber", physTask.getTaskNumber());
        context.setVariable("selfTaskNumber", physTask.getSelfTaskNumber());
        context.setVariable("taskLevel", physTask.getTaskLevel());
        context.setVariable("taskText", physTask.getTaskText());
        context.setVariable("pageNumber", physTask.getPageNumber());

        String text = engine.process("phys_task_message", context);

        var keyboard = keyboardBuilder.buildBackToMainMenu();

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, text, ParseMode.HTML);

    }
}
