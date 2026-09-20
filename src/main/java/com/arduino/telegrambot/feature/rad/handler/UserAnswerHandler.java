package com.arduino.telegrambot.feature.rad.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.NumberSystem;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import com.arduino.telegrambot.feature.rad.validator.AnswerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order()

public class UserAnswerHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private AnswerValidator answerValidator;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserState.WAIT_USER_RAD_ANSWER.equals(userService.findById(userRequest.getChatId()).getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var userAnswer = userRequest.getHandler();

        String taskValue = user.getTask();
        var result = answerValidator.validateAnswer(taskValue, userAnswer);

        String source = NumberSystem.valueOf(taskValue.substring(0, 3)).getDativ();
        String target = NumberSystem.valueOf(taskValue.substring(3, 6)).getAkusativ();
        String taskNumber = taskValue.substring(6);


        var text = templateProcessor.processRadUserResultMessageTemplate(result.isResult(), result.getRightAnswer(), result.getUserAnswer(), source, target, taskNumber);
        var keyboard = keyboardBuilder.buildCompletedTaskMenu();

        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getMessageId());
        telegramService.editRichMessage(userRequest.getChatId(), user.getMessageId(), keyboard, text);
        user.setState(UserState.FREE);
        user.setMessageId(0L);
        user.setTask("");
        userService.save(user);
    }
}
