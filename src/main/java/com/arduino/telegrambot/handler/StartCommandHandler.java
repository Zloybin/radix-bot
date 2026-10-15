package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return handlerCallbacks.contains(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
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

        var media = telegramMediaService.findByKey("header");

        var userStatistics = UserStatistics.builder()
                .completedDuoCardsSentenceTaskCount(user.getDaysStrikeInfoList().get(today).getCompletedDuoCardsSentenceTaskCount())
                .completedPhysTaskCount(user.getDaysStrikeInfoList().get(today).getCompletedPhysTaskCount())
                .weekStrikeInfo(weekStrikeInfo)
                .build();

        var text = templateProcessor.processGreetingsTemplate(userStatistics);
        var keyboard = keyboardBuilder.buildMainMenu();

        if ("/start".equals(userRequest.getHandler())) {
            user.setState(UserState.FREE);
//            telegramService.sendRichMessage(userRequest.getChatId(), keyboard, text);
            telegramService.sendRichMessageWithMedia(userRequest.getChatId(), keyboard, text, media);
        } else {
//            telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
            telegramService.editRichMessageWithMedia(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text, media);
        }
    }
}
