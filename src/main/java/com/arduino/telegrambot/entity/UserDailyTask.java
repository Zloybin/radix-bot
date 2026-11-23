package com.arduino.telegrambot.entity;

import com.arduino.telegrambot.update.enumeration.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_daily_tasks")
@Getter
@Setter
public class UserDailyTask {

    @EmbeddedId
    private UserDailyTaskId userDailyTaskId;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @MapsId("taskId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private boolean requiredForStrikeSnapshot;
}