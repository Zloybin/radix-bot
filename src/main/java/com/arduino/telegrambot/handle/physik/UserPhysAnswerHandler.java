package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.ResultService;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import com.arduino.telegrambot.validator.AnswerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

@Component
public class UserPhysAnswerHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private AnswerValidator answerValidator;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private TaskService taskService;

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
        var userAnswer = userRequest.getHandler();

        var result = answerValidator.validatePhysAnswer(user.getPhysTaskId(), userAnswer);

        resultService.save(result);
        user.getResults().add(result);
        user.setState(UserState.FREE);
        userService.save(user);

        var physTask = taskService.findById(user.getPhysTaskId());

        var section = physTask.getSection().getRussianName();
        var title = physTask.getTitle();
        var taskNumber = physTask.getTaskNumber();
        var selfTaskNumber = physTask.getSelfTaskNumber();
        var taskLevel = physTask.getTaskLevel().getTitle();
        var taskText = physTask.getTaskText();
        var pageNumber = physTask.getPageNumber();

        var text = templateProcessor.processUserResultMessageTemplate(result.isResult(), result.getTask().getAnswer(), result.getUserAnswer(), section, title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber);

        var keyboard = keyboardBuilder.buildCompletedPhysTaskWithCorrectMenu();
        var infoButton = keyboardBuilder.buildInfoResultButton(result.isResult());

//        telegramService.editKeyboard(userRequest.getChatId(), (int) user.getMessageId(), infoButton);
        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getMessageId());
        telegramService.editRichMessage(userRequest.getChatId(), (int) user.getMessageId(), keyboard, text);
    }
}
