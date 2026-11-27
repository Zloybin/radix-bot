package com.arduino.telegrambot.provider;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.TaskType;
import com.arduino.telegrambot.enummeration.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ManualTaskStatusProvider
        implements TaskStatusProvider {

//    private final UserDailyTaskRepository repository;

    @Override
    public TaskType getSource() {
        return TaskType.MEDITATION;
    }

    @Override
    public TaskStatus getStatus(
            User user,
            LocalDate date
    ) {
        return TaskStatus.NOT_COMPLETED;
//        return repository
//                .findByUserAndTaskAndDate(user, task, date)
//                .map(UserDailyTask::getStatus)
//                .orElse(TaskStatus.NOT_COMPLETED);
    }
}