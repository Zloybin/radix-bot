package com.arduino.telegrambot.statistick.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.model.DailyStrikeInfo;
import com.arduino.telegrambot.repository.UserDailyTaskRepository;
import com.arduino.telegrambot.entity.UserDailyTask;
import com.arduino.telegrambot.update.enumeration.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class StrikeCalculationServiceImpl implements StrikeCalculationService{

    public static final DayOfWeek MONDAY = DayOfWeek.MONDAY;
    public static final DayOfWeek SUNDAY = DayOfWeek.SUNDAY;

    @Autowired
    private UserDailyTaskRepository userDailyTaskRepository;

    @Override
    public boolean isStrikeCompleted(User user, LocalDate date) {

        var dailyTasks =
                userDailyTaskRepository.findAllByUserAndUserDailyTaskId_Date(user, date);

        var requiredTasks =
                dailyTasks.stream()
                        .filter(UserDailyTask::isRequiredForStrikeSnapshot)
                        .toList();

        return !requiredTasks.isEmpty()
                && requiredTasks.stream()
                .allMatch(task -> task.getStatus() == TaskStatus.COMPLETED);
    }

    @Override
    public List<DailyStrikeInfo> getStrikeActualWeek(User user) {

        var today = LocalDate.now(ZoneId.systemDefault());
        var monday = today.with(MONDAY);
        var sunday = today.with(SUNDAY);

        List<DailyStrikeInfo> dailyStrikeInfoList = new ArrayList<>();

        for (LocalDate date = sunday; date.isAfter(monday) || date.isEqual(monday); date = date.minusDays(1)) {
            if (date.isAfter(today)) {

                var dayStrikeInfo = DailyStrikeInfo.builder()
                        .date(date)
                        .day(date.getDayOfWeek()
                                .getDisplayName(TextStyle.SHORT, new Locale("ru")))
                        .taskStatus(TaskStatus.UNKNOWN)
                        .build();

                dailyStrikeInfoList.add(dayStrikeInfo);
            } else {

                var dayStrikeInfo = DailyStrikeInfo.builder()
                        .date(date)
                        .day(date.getDayOfWeek()
                                .getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("ru")))
                        .taskStatus(isStrikeCompleted(user, date) ? TaskStatus.COMPLETED : TaskStatus.NOT_COMPLETED)
                        .build();

                dailyStrikeInfoList.add(dayStrikeInfo);

            }
        }
        return dailyStrikeInfoList.reversed();
    }

    @Override
    public int getActualStrikeValue(Long userId) {

        List<LocalDate> failedDateList = userDailyTaskRepository.findFailedDateList(userId);

        if (failedDateList.isEmpty()) {
            return 0;
        }
        LocalDate lastFailedDate = failedDateList.get(0);
        int kof = 0;
        var today = LocalDate.now(ZoneId.systemDefault());

        if (today.isEqual(lastFailedDate)) {
            if (failedDateList.size() < 2) {
                return 0;
            } else {
                kof = 1;
                lastFailedDate = failedDateList.get(1);
            }

        }

        if (lastFailedDate == today.minusDays(1)) {
            return kof;
        }

        int strikeWithoutToday = (int) ChronoUnit.DAYS.between(lastFailedDate, today);
        return strikeWithoutToday - kof;

    }
}
