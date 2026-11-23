package com.arduino.telegrambot.feature.duocards.sentence.repository;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.duocards.sentence.entity.DuoCardsSentenceDailyInfo;
import com.arduino.telegrambot.feature.duocards.sentence.entity.DuoCardsSentenceDailyInfoId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DuoCardsSentenceDailyInfoRepository extends CrudRepository<DuoCardsSentenceDailyInfo, DuoCardsSentenceDailyInfoId> {
    DuoCardsSentenceDailyInfo findAllByUserAndDuoCardsSentenceDailyInfoId_Date(User user, LocalDate date);
}
