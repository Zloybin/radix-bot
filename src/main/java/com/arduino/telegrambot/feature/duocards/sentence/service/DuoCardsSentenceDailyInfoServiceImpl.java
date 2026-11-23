package com.arduino.telegrambot.feature.duocards.sentence.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.duocards.sentence.entity.DuoCardsSentenceDailyInfo;
import com.arduino.telegrambot.feature.duocards.sentence.repository.DuoCardsSentenceDailyInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class DuoCardsSentenceDailyInfoServiceImpl implements DuoCardsSentenceDailyInfoService{
    @Autowired
    private DuoCardsSentenceDailyInfoRepository duoCardsSentenceDailyInfoRepository;

    @Override
    public DuoCardsSentenceDailyInfo getByUserAndDate(User user, LocalDate date){
        return duoCardsSentenceDailyInfoRepository.findAllByUserAndDuoCardsSentenceDailyInfoId_Date(user, date);
    }

    @Override
    public DuoCardsSentenceDailyInfo getCompletedDuoCardsSentenceToday(User user){
        var today = LocalDate.now(ZoneId.systemDefault());
        return getByUserAndDate(user, today);
    }

    @Override
    public DuoCardsSentenceDailyInfo save(DuoCardsSentenceDailyInfo duoCardsSentenceDailyInfo) {
        return duoCardsSentenceDailyInfoRepository.save(duoCardsSentenceDailyInfo);
    }


}
