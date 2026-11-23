package com.arduino.telegrambot.update.repository;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.entity.AnkiDeckDailyInfo;
import com.arduino.telegrambot.feature.anki.entity.AnkiDeckDailyInfoId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnkiDeckDailyInfoRepository extends CrudRepository<AnkiDeckDailyInfo, AnkiDeckDailyInfoId> {
    List<AnkiDeckDailyInfo> findAllByUserAndAnkiDeckDailyInfoId_Date(User user, LocalDate date);
}
