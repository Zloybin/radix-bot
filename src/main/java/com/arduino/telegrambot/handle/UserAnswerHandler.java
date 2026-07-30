package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.TaskResult;
import com.arduino.telegrambot.model.User;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.validator.AnswerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class UserAnswerHandler implements UpdateHandler{

    @Autowired
    private UserService userService;

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
        return UserState.WAIT_USER_RAD_ANSWER.equals(userService.getUser(userRequest.getChatId()).getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.getUser(userRequest.getChatId());
        var userAnswer = userRequest.getRequest();

        var result = answerValidator.validateAnswer(user.getTask(), userAnswer);

        Context context = new Context();

        context.setVariable("result", result.isResult());
        context.setVariable("rightAnswer", result.getRightAnswer());
        context.setVariable("userAnswer", result.getUserAnswer());
        String message = engine.process("user_result_message", context);

        var keyboard = keyboardBuilder.buildBackToMainMenu();
        user.setState(UserState.FREE);
        userService.putUser(user.getId(), user);

        telegramService.sendMessageWithKeyboard(userRequest.getChatId(), keyboard, message, ParseMode.HTML);
    }
}
