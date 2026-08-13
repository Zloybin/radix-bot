package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.ResultService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.validator.AnswerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class UserPhysAnswerHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private AnswerValidator answerValidator;

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserState.WAIT_USER_PHYS_ANSWER.equals(userService.findById(userRequest.getChatId()).getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var userAnswer = userRequest.getRequest();

        var result = answerValidator.validatePhysAnswer((long) user.getPhysTask(), userAnswer);

        resultService.save(result);
        user.getResults().add(result);

        Context context = new Context();

        context.setVariable("result", result.isResult());
        context.setVariable("rightAnswer", result.getTask().getAnswer());
        context.setVariable("userAnswer", result.getUserAnswer());

        String message = engine.process("user_result_message", context);

        var keyboard = keyboardBuilder.buildCompletedPhysTaskMenu();

        user.setPhysTask(0);
        user.setState(UserState.FREE);
        userService.save(user);

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, message, ParseMode.HTML);
    }
}
