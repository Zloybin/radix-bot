package com.arduino.telegrambot.provider;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.TaskStatus;
import com.arduino.telegrambot.enummeration.TaskType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PullUpsTaskStatusProvider implements TaskStatusProvider{
    @Override
    public TaskType getSource() {
        return TaskType.PULLUPS;
    }

    @Override
    public TaskStatus getStatus(User user, LocalDate date) {
        return TaskStatus.NOT_COMPLETED;
    }
}
