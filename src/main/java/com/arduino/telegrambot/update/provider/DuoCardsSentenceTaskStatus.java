package com.arduino.telegrambot.update.provider;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.update.enumeration.TaskType;
import com.arduino.telegrambot.update.enumeration.TaskStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DuoCardsSentenceTaskStatus implements TaskStatusProvider{
    @Override
    public TaskType getSource() {
        return TaskType.DUO_CARDS_SENTENCE;
    }

    @Override
    public TaskStatus getStatus(User user, LocalDate date) {
        return TaskStatus.UNKNOWN;
    }
}
