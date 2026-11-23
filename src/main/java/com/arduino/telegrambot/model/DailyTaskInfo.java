package com.arduino.telegrambot.model;

import com.arduino.telegrambot.update.enumeration.TaskStatus;
import com.arduino.telegrambot.update.enumeration.TaskType;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class DailyTaskInfo {
    private TaskStatus taskStatus;
    private TaskType taskType;
    private String taskDescription;
}
