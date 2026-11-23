package com.arduino.telegrambot.feature.physik.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.entity.PhysicsTaskDailyInfo;
import com.arduino.telegrambot.feature.physik.repository.PhysicsTaskDailyInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PhysicsTaskDailyInfoServiceImpl implements PhysicsTaskDailyInfoService{

    @Autowired
    private PhysicsTaskDailyInfoRepository physicsTaskDailyInfoRepository;

    @Override
    public PhysicsTaskDailyInfo getByUserAndDate(User user, LocalDate localDate) {
        return physicsTaskDailyInfoRepository.findAllByUserAndPhysicsTaskDailyInfoId_Date(user, localDate);
    }

    @Override
    public PhysicsTaskDailyInfo save(PhysicsTaskDailyInfo physicsTaskDailyInfo) {
        return physicsTaskDailyInfoRepository.save(physicsTaskDailyInfo);
    }
}
