package com.arduino.telegrambot.update;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.update.enumeration.TaskType;
import com.arduino.telegrambot.update.enumeration.TaskStatus;
import com.arduino.telegrambot.update.provider.TaskStatusProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskStatusService {

    private final Map<TaskType, TaskStatusProvider> providers;

    public TaskStatusService(
            List<TaskStatusProvider> providerList
    ) {
        this.providers = providerList.stream()
                .collect(Collectors.toMap(
                        TaskStatusProvider::getSource,
                        Function.identity()
                ));
    }

    public TaskStatus getStatus(
            User user,
            TaskType taskType,
            LocalDate date
    ) {
        TaskStatusProvider provider =
                providers.get(taskType);

        if (provider == null) {
            throw new IllegalStateException(
                    "Provider not found: " + taskType
            );
        }

        return provider.getStatus(user, date);
    }
}