package com.arduino.telegrambot.entity;

import com.arduino.telegrambot.enummeration.UserState;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


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
    @Enumerated(value = EnumType.STRING)
    private UserState state;

    @Column
    private String task;

    @Column
    private String name;

    @Column
    private long physTaskId;

    @Column
    private long messageId;

    @Column
    private boolean isExcluded;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Result> results;
}
