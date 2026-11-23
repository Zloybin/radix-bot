package com.arduino.telegrambot.feature.physik.handler;

import com.arduino.telegrambot.feature.ai.LLMService;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.feature.physik.entity.Result;
import com.arduino.telegrambot.feature.physik.service.PhysicsTaskService;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AskAiHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private PhysicsTaskService physicsTaskService;

    @Autowired
    private UserService userService;

    @Autowired
    private LLMService llmService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "askAi".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var task = physicsTaskService.findById(user.getPhysTaskId());
        var taskText = task.getTaskText();


        String userAnswer = "";

        List<Result> results = user.getResults();
        for (int i = results.size()-1; i >= 0 ; i--) {
            Result result = results.get(i);
            if (result.getPhysicsTask().getId().equals(task.getId())){
                 userAnswer = result.getUserAnswer();
                 break;
            }
        }

        var title = task.getTitle();
        var section = task.getSection().getRussianName();
        var taskNumber = task.getTaskNumber();
        var selfTaskNumber = task.getSelfTaskNumber();
        var taskLevel = task.getTaskLevel().getTitle();
        var pageNumber = task.getPageNumber();
        var rightAnswer = task.getAnswer();


        var keyboard = keyboardBuilder.buildCompletedPhysTaskWithCorrectMenuWithoutAi();

        var waitResponseText = templateProcessor.processPhysTaskWaitAiTemplate(title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber, section, userAnswer, rightAnswer);
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, waitResponseText);

        var response = llmService.process(taskText, userAnswer);
        var text = templateProcessor.processPhysTaskWithAiTemplate(title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber, section, response, userAnswer, rightAnswer);
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
