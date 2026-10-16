package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.*;
import com.arduino.telegrambot.statistick.processor.StatisticsProcessor;
import com.arduino.telegrambot.service.TelegramMediaService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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

    @Autowired
    private TelegramMediaService telegramMediaService;

    @Autowired
    private StatisticsProcessor statisticsProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return handlerCallbacks.contains(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
        var userStatistics = statisticsProcessor.getUserStatistics(user);
        userService.save(user);

        var text = templateProcessor.processGreetingsTemplate(userStatistics);
        var keyboard = keyboardBuilder.buildMainMenu();
//        var media = telegramMediaService.findByKey("header");

        if ("/start".equals(userRequest.getHandler())) {
            user.setState(UserState.FREE);
            telegramService.sendRichMessage(userRequest.getChatId(), keyboard, text);
//            telegramService.sendRichMessageWithMedia(userRequest.getChatId(), keyboard, text, media);
        } else {
            telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
//            telegramService.editRichMessageWithMedia(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text, media);
        }
    }


}
