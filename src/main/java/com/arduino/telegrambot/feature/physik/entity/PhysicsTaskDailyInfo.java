package com.arduino.telegrambot.feature.physik.entity;

import com.arduino.telegrambot.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "physics_task_daily_info")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhysicsTaskDailyInfo {
    @EmbeddedId
    private PhysicsTaskDailyInfoId physicsTaskDailyInfoId;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "completed_physics_task_count")
    private int completedPhysicsTaskCount;

    @Column(name = "physics_task_goal")
    private int physicsTaskGoal;
}