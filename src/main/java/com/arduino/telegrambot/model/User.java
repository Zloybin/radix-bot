package com.arduino.telegrambot.model;

import com.arduino.telegrambot.enummeration.UserState;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private long id;

    @Column
    private UserState state;

    @Column
    private String task;

    @Column
    private int physTask;
}
