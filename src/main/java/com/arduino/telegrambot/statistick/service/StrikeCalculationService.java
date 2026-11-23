package com.arduino.telegrambot.statistick.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.model.DailyStrikeInfo;

import java.time.LocalDate;
import java.util.List;

public interface StrikeCalculationService {
    boolean isStrikeCompleted(User user, LocalDate date);
    List<DailyStrikeInfo> getStrikeActualWeek(User user);
    int getActualStrikeValue(Long userId);
}
