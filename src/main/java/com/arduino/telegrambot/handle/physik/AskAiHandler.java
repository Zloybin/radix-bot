package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.ai.LLMService;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.Task;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

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


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "askAi".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var task = taskService.findById(user.getPhysTaskId());
        var taskText = task.getTaskText();

        var response = llmService.process(taskText);

        var title = task.getTitle();
        var taskNumber = task.getTaskNumber();
        var selfTaskNumber = task.getSelfTaskNumber();
        var taskLevel = task.getTaskLevel().getTitle();
        var pageNumber = task.getPageNumber();
        var text = templateProcessor.processPhysTaskWithAiTemplate(title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber, response);
        var keyboard = keyboardBuilder.buildPhysTaskMenu();

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
    }
}
