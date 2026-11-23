package com.arduino.telegrambot.update.provider;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.update.enumeration.TaskType;
import com.arduino.telegrambot.update.enumeration.TaskStatus;

import java.time.LocalDate;

public interface TaskStatusProvider {

    TaskType getSource();

    TaskStatus getStatus(User user, LocalDate date);
}