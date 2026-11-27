package com.arduino.telegrambot.model;

import com.arduino.telegrambot.enummeration.TaskStatus;
import com.arduino.telegrambot.enummeration.TaskType;
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
