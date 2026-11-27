package com.arduino.telegrambot.provider;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.TaskType;
import com.arduino.telegrambot.enummeration.TaskStatus;

import java.time.LocalDate;

public interface TaskStatusProvider {

    TaskType getSource();

    TaskStatus getStatus(User user, LocalDate date);
}