package com.arduino.telegrambot.feature.physik.service;


import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.entity.PhysicsTaskDailyInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface PhysicsTaskDailyInfoService {
    PhysicsTaskDailyInfo getByUserAndDate(User user, LocalDate localDate);

    PhysicsTaskDailyInfo save(PhysicsTaskDailyInfo physicsTaskDailyInfo);
}
