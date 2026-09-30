package com.arduino.telegrambot.feature.duocards.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.DuoCardExample;
import com.arduino.telegrambot.feature.ai.LLMService;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangeExampleHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private LLMService llmService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private TelegramService telegramService;



    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "changeExample".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var sentance = user.getDuoCardExample().getSentence();


        var duoCardExample = llmService.processTranslationMatching(sentance);

        user.setDuoCardExample(duoCardExample);
        userService.save(user);

        var keyboard = keyboardBuilder.buildProcessNewDuoCardKeyboard();
        var text = templateProcessor.processExampleNewCard(duoCardExample.getSentence(), duoCardExample.getTranslation(), duoCardExample.getExample());
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
