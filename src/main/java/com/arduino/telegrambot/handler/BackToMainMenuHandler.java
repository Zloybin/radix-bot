package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.statistick.processor.StatisticsProcessor;
import com.arduino.telegrambot.service.TelegramMediaService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackToMainMenuHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private TelegramMediaService telegramMediaService;

    @Autowired
    private StatisticsProcessor statisticsProcessor;


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "backToMainMenu".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var chatId = userRequest.getChatId();
        var user = userService.findById(chatId);

        user.setState(UserState.FREE);
        var userStatistics = statisticsProcessor.getUserStatistics(user);
        userService.save(user);

        var text = templateProcessor.processGreetingsTemplate(userStatistics);
        var keyboard = keyboardBuilder.buildMainMenu();
//        var media = telegramMediaService.findByKey("header");

        telegramService.editRichMessage(chatId, userRequest.getMessageId(), keyboard, text);
//        telegramService.editRichMessageWithMedia(chatId, userRequest.getMessageId(), keyboard, text, media);

    }
}
