package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.duocards.DuoCardsSentenceUtil;
import com.arduino.telegrambot.feature.duocards.sentence.entity.DuoCardsSentenceDailyInfo;
import com.arduino.telegrambot.feature.duocards.sentence.entity.DuoCardsSentenceDailyInfoId;
import com.arduino.telegrambot.feature.duocards.sentence.service.DuoCardsSentenceDailyInfoService;
import com.arduino.telegrambot.feature.physik.service.PhysicsTaskService;
import com.arduino.telegrambot.feature.physik.service.ResultService;
import com.arduino.telegrambot.feature.physik.util.PhysicsTaskSettingValueUtil;
import com.arduino.telegrambot.model.*;
import com.arduino.telegrambot.repository.UserDailyTaskRepository;
import com.arduino.telegrambot.statistick.service.StatisticsCalculationService;
import com.arduino.telegrambot.statistick.service.StrikeCalculationService;
import com.arduino.telegrambot.service.TelegramMediaService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import com.arduino.telegrambot.update.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class StartCommandHandler implements UpdateHandler {
    public static final DayOfWeek MONDAY = DayOfWeek.MONDAY;
    public static final DayOfWeek SUNDAY = DayOfWeek.SUNDAY;
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
    private TaskStatusService taskStatusService;

    @Autowired
    private AnkiService ankiService;

    @Autowired
    private StrikeCalculationService strikeCalculationService;

    @Autowired
    private StatisticsCalculationService statisticsCalculationService;

    @Autowired
    private PhysicsTaskService physicsTaskService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private DuoCardsSentenceDailyInfoService duoCardsSentenceDailyInfoService;

    @Autowired
    private UserDailyTaskRepository userDailyTaskRepository;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return handlerCallbacks.contains(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());

        String firstname = user.getFirstname();
        String lastname = user.getLastname();
        var dailyTaskInfoList = statisticsCalculationService.getTodayDailyTaskInfoList(user);
        var strikeInfoList = strikeCalculationService.getStrikeActualWeek(user);
        var completedPhysicsTaskCount = resultService.countCompletedPhysTaskToday(user);
        int physicsTaskGoalValue = PhysicsTaskSettingValueUtil.PHYSICS_TASK_GOAL_VALUE;
        var duoCardsSentenceDailyInfo = duoCardsSentenceDailyInfoService.getCompletedDuoCardsSentenceToday(user);

        if(duoCardsSentenceDailyInfo == null){
            var duoCardsSentenceDailyInfoId = DuoCardsSentenceDailyInfoId.builder()
                    .date(LocalDate.now(ZoneId.systemDefault()))
                    .userId(user.getId())
                    .build();

            duoCardsSentenceDailyInfo = DuoCardsSentenceDailyInfo.builder()
                    .duoCardsSentenceDailyInfoId(duoCardsSentenceDailyInfoId)
                    .completedDuoCardsSentenceTaskCount(0)
                    .user(user)
                    .duoCardsSentenceGoal(DuoCardsSentenceUtil.PHYSICS_TASK_COUNT)
                    .build();
            duoCardsSentenceDailyInfoService.save(duoCardsSentenceDailyInfo);
        }
        int actualStrikeValue = strikeCalculationService.getActualStrikeValue(user.getId());
        int updatedBestStrikeValue = getUpdatedBestStrikeValue(user, actualStrikeValue);

        User savedUser = userService.save(user);

        var text = templateProcessor.processGreetingsTemplate(firstname, lastname, dailyTaskInfoList, strikeInfoList, completedPhysicsTaskCount, physicsTaskGoalValue, duoCardsSentenceDailyInfo.getCompletedDuoCardsSentenceTaskCount(), duoCardsSentenceDailyInfo.getDuoCardsSentenceGoal(), actualStrikeValue, updatedBestStrikeValue);
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

    private int getUpdatedBestStrikeValue(User user, int actualStrikeValue) {
        int bestStrikeValue = user.getBestStrikesCount();
        int updatedBestStrikeValue = actualStrikeValue > bestStrikeValue
                ? actualStrikeValue
                : bestStrikeValue;

        if (bestStrikeValue != updatedBestStrikeValue) {
            user.setBestStrikesCount(actualStrikeValue);
        }

        return updatedBestStrikeValue;
    }
}
