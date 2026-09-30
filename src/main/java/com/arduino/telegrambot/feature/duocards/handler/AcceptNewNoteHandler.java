package com.arduino.telegrambot.feature.duocards.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.DuoCardExample;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.feature.ai.LLMService;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AcceptNewNoteHandler implements UpdateHandler {

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
        var user = userService.findById(userRequest.getChatId());
        return UserState.ADD_NEW_NOTE_MODE.equals(user.getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        User user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        var messageId = user.getMessageId();
        user.setMessageId(0L);


        var newNote = userRequest.getHandler();
        var duoCardExample = llmService.processTranslationMatching(newNote);

        user.setDuoCardExample(duoCardExample);
        userService.save(user);

        var keyboard = keyboardBuilder.buildProcessNewDuoCardKeyboard();
        var text = templateProcessor.processExampleNewCard(duoCardExample.getSentence(), duoCardExample.getTranslation(), duoCardExample.getExample());
        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getMessageId());
        telegramService.editRichMessage(userRequest.getChatId(), messageId, keyboard, text);


    }
}
