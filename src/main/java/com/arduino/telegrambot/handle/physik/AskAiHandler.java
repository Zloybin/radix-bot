package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.ai.LLMService;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.Result;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.ResultService;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

import java.util.List;

@Component
public class AskAiHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private LLMService llmService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private ResultService resultService;


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "askAi".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var task = taskService.findById(user.getPhysTaskId());
        var taskText = task.getTaskText();


        String userAnswer = "";

        List<Result> results = user.getResults();
        for (int i = results.size()-1; i >= 0 ; i--) {
            Result result = results.get(i);
            if (result.getTask().getId().equals(task.getId())){
                 userAnswer = result.getUserAnswer();
                 break;
            }
        }

        var title = task.getTitle();
        var taskNumber = task.getTaskNumber();
        var selfTaskNumber = task.getSelfTaskNumber();
        var taskLevel = task.getTaskLevel().getTitle();
        var pageNumber = task.getPageNumber();
        var keyboard = keyboardBuilder.buildCompletedPhysTaskWithCorrectMenuWithoutAi();

        var waitResponseText = templateProcessor.processPhysTaskWaitAiTemplate(title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber);
        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), waitResponseText, keyboard, ParseMode.HTML);

        var response = llmService.process(taskText, userAnswer);
        var text = templateProcessor.processPhysTaskWithAiTemplate(title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber, response, userAnswer);
        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
    }
}
