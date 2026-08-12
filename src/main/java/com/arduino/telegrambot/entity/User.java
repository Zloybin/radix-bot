package com.arduino.telegrambot.entity;

import com.arduino.telegrambot.enummeration.UserState;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Table(name = "users")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private long id;

    @Column
    private UserState state;

    @Column
    private String task;

    @Column
    private int physTask;

    @OneToMany
    @JoinColumn(name = "tasks", referencedColumnName = "id")
    private Set<Task> tasks;
}
