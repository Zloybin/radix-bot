package com.arduino.telegrambot.feature.duocards.sentence.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.duocards.sentence.entity.DuoCardsSentenceDailyInfo;

import java.time.LocalDate;

public interface DuoCardsSentenceDailyInfoService {
    DuoCardsSentenceDailyInfo getByUserAndDate(User user, LocalDate date);
    DuoCardsSentenceDailyInfo getCompletedDuoCardsSentenceToday(User user);

    DuoCardsSentenceDailyInfo save(DuoCardsSentenceDailyInfo duoCardsSentenceDailyInfo);
}
