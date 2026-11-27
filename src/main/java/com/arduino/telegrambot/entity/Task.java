package com.arduino.telegrambot.entity;

import com.arduino.telegrambot.enummeration.TaskType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private String name;

    private boolean active;

    private boolean requiredForStrike;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;
}