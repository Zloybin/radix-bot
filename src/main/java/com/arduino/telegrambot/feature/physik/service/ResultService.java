package com.arduino.telegrambot.feature.physik.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.entity.Result;
import com.arduino.telegrambot.feature.physik.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    public Result save(Result result){
        return resultRepository.save(result);
    }

    public int getCompletedTaskInSectionFromUserCount(Long userId, String section) {
        return resultRepository.countPassedResultsByUserAndSection(userId, section);
    }

    public int getFailedTaskInSectionFromUserCount(Long userId, String section) {
        return resultRepository.countFailedResultsByUserAndSection(userId, section);
    }

    public int countByUserAndDueDate(User user, LocalDate date) {
        return resultRepository.countByUserAndDueDate(user, date);
    }

    public int countCompletedPhysTaskToday(User user) {
        var today = LocalDate.now(ZoneId.systemDefault());
        return resultRepository.countByUserAndDueDate(user, today);
    }

}
