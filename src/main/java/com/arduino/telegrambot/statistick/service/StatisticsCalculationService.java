package com.arduino.telegrambot.statistick.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.model.DailyTaskInfo;

import java.time.LocalDate;
import java.util.List;

public interface StatisticsCalculationService {
    List<DailyTaskInfo> getDailyTaskInfoList(User user, LocalDate localDate);
    List<DailyTaskInfo> getTodayDailyTaskInfoList(User user);
}
