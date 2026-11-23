package com.arduino.telegrambot.feature.physik.repository;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.entity.PhysicsTaskDailyInfo;
import com.arduino.telegrambot.feature.physik.entity.PhysicsTaskDailyInfoId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PhysicsTaskDailyInfoRepository extends CrudRepository<PhysicsTaskDailyInfo, PhysicsTaskDailyInfoId> {
    PhysicsTaskDailyInfo findAllByUserAndPhysicsTaskDailyInfoId_Date(User user, LocalDate date);
}
