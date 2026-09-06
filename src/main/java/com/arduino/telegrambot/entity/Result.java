package com.arduino.telegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "task", referencedColumnName = "id")
    private Task task;
}
