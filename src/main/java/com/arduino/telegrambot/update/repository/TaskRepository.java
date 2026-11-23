package com.arduino.telegrambot.update.repository;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.entity.Task;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Task findByCode(String code);
    List<Task> findByRequiredForStrikeAndActive(boolean isRequiredForStrike, boolean isActive);
}
