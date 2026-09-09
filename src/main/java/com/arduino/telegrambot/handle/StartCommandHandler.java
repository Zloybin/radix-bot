package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

import java.util.List;

@Component
public class StartCommandHandler implements UpdateHandler {
    private final List<String> handlerCallbacks = List.of("/start", "start");

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;



    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return handlerCallbacks.contains(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var text = templateProcessor.processGreetingsTemplate();
        var keyboard = keyboardBuilder.buildMainMenu();

        if ("/start".equals(userRequest.getHandler())) {
            var user = userService.findById(userRequest.getChatId());
            user.setState(UserState.FREE);
            telegramService.sendRichMessage(userRequest.getChatId(), keyboard, text);
        } else {
            telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
        }
    }
}
