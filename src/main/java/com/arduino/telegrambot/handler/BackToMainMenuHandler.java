package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.TelegramMedia;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.DayStrikeInfo;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.model.UserStatistics;
import com.arduino.telegrambot.service.TelegramMediaService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class BackToMainMenuHandler implements UpdateHandler{

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


    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "backToMainMenu".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var chatId = userRequest.getChatId();
        var user = userService.findById(chatId);
        user.setState(UserState.FREE);
        var daysStrikeInfoList = user.getDaysStrikeInfoList();
        var today = LocalDate.now();
        var day = today.with(DayOfWeek.SUNDAY);

        Map<LocalDate, Boolean> weekStrikeInfo = new HashMap<>();

        var startOfWeek = today.with(DayOfWeek.MONDAY);
        while(!day.isBefore(startOfWeek)){
            var dayStrikeInfo = daysStrikeInfoList.get(day);

            if((day.equals(today) || day.isBefore(today)) && dayStrikeInfo == null){
                dayStrikeInfo = DayStrikeInfo.builder()
                        .isAnkiDecksCompleted(false)
                        .completedPhysTaskCount(0)
                        .completedDuoCardsSentenceTaskCount(0)
                        .build();
                user.getDaysStrikeInfoList().put(day, dayStrikeInfo);
            }

            userService.save(user);

            weekStrikeInfo.put(day, dayStrikeInfo == null ? null : dayStrikeInfo.isDaySuccesses());
            day = day.minusDays(1L);
        }
        userService.save(user);

        var userStatistics = UserStatistics.builder()
                .completedDuoCardsSentenceTaskCount(user.getDaysStrikeInfoList().get(today).getCompletedDuoCardsSentenceTaskCount())
                .completedPhysTaskCount(user.getDaysStrikeInfoList().get(today).getCompletedPhysTaskCount())
                .weekStrikeInfo(weekStrikeInfo)
                .build();

        var keyboard = keyboardBuilder.buildMainMenu();
        TelegramMedia media = telegramMediaService.findByKey("header");
        var text = templateProcessor.processGreetingsTemplate(userStatistics);

//        telegramService.editRichMessage(chatId, userRequest.getMessageId(), keyboard, text);
        telegramService.editRichMessageWithMedia(chatId, userRequest.getMessageId(), keyboard, text, media);

    }
}
