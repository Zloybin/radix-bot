package com.arduino.telegrambot.feature.physik.entity;

import com.arduino.telegrambot.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "results")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private boolean result;

    @Column
    private String userAnswer;

    @Column
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "task", referencedColumnName = "id")
    private PhysicsTask physicsTask;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
