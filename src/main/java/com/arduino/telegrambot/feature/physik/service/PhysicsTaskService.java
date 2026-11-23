package com.arduino.telegrambot.feature.physik.service;

import com.arduino.telegrambot.feature.physik.entity.PhysicsTask;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.enummeration.Section;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface PhysicsTaskService {
    PhysicsTask findById(Long id);
    long count();
    int getRandomPhysTaskId();
    Long getRandomPhysTaskId(List<Long> completedTaskIds);
    int getSectionTaskCount(Section section);
}
