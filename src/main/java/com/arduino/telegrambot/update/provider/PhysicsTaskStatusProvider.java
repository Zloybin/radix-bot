package com.arduino.telegrambot.update.provider;

import com.arduino.telegrambot.feature.physik.service.PhysicsTaskService;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.physik.service.ResultService;
import com.arduino.telegrambot.update.enumeration.TaskType;
import com.arduino.telegrambot.update.enumeration.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PhysicsTaskStatusProvider
        implements TaskStatusProvider {

    private final ResultService resultService;

    @Override
    public TaskType getSource() {
        return TaskType.PHYSICS;
    }

    @Override
    public TaskStatus getStatus(User user, LocalDate date) {
        int completedPhysicsTaskCount = resultService.countByUserAndDueDate(user, date);
        return completedPhysicsTaskCount >= 5
                ? TaskStatus.COMPLETED
                : TaskStatus.NOT_COMPLETED;

    }
}