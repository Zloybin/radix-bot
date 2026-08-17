package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
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
public class PhysTaskHandler implements UpdateHandler {

    private final String handlerCallback = "physTask";

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return handlerCallback.equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {

        int randomTaskId;

        var user = userService.findById(userRequest.getChatId());

        if(user.getPhysTaskId() == 0){

            randomTaskId = taskService.getRandomPhysTaskId();
            user.setPhysTaskId(randomTaskId);
            userService.save(user);

        }else{
            randomTaskId = user.getPhysTaskId();
        }

        var physTask = taskService.findById((long) randomTaskId);

        Context context = new Context();
        context.setVariable("title", physTask.getTitle());
        context.setVariable("taskNumber", physTask.getTaskNumber());
        context.setVariable("selfTaskNumber", physTask.getSelfTaskNumber());
        context.setVariable("taskLevel", physTask.getTaskLevel().getTitle());
        context.setVariable("taskText", physTask.getTaskText());
        context.setVariable("pageNumber", physTask.getPageNumber());

        String caption = engine.process("phys_task_message", context);

        var keyboard = keyboardBuilder.buildPhysTaskMenu();

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), caption, keyboard, ParseMode.HTML);

    }
}
