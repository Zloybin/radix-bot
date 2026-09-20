package com.arduino.telegrambot.feature.physik.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.ResultService;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "changeToFalse".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var physTask = user.getPhysTaskId();
        var task = taskService.findById(physTask);
        var results = user.getResults();
        var actualResult = results.stream()
                .filter(result -> result.getTask().getId().equals(task.getId()))
                .findFirst();

        boolean updatedResultValue = false;
        var infoButton = keyboardBuilder.buildInfoResultButton(updatedResultValue);
        actualResult.ifPresent(result -> {
            result.setResult(updatedResultValue);
            resultService.save(result);
            System.out.println(String.format("Результат с id: %s был изменен на значение: false", result.getId()));
        });

        user.setPhysTaskId(0L);
        user.setMessageId(0L);
        userService.save(user);

        var keyboard = keyboardBuilder.buildCompletedPhysTaskMenu();
        var text = templateProcessor.processSuccessCorrectTemplate(task.getId(), updatedResultValue);


        telegramService.editRichMessageKeyboard(userRequest.getChatId(), userRequest.getMessageId(), infoButton);
        telegramService.sendRichMessage(userRequest.getChatId(), keyboard, text);
    }
}
