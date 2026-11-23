package com.arduino.telegrambot.entity;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.feature.duocards.sentence.model.GeneratedExample;
import com.arduino.telegrambot.feature.duocards.web.entity.DuoCardExample;
import com.arduino.telegrambot.feature.physik.entity.Result;
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
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String username;

    @Column
    private int bestStrikesCount;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserState state;

    @Column
    private long physTaskId;

    @Column
    private String radTask;

    @Column
    private long messageId;

    @Column
    private boolean isExcluded;

    @Column
    private boolean isAnswerSide;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Result> results;

    @Embedded
    private GeneratedExample generatedExample;

    @Embedded
    private DuoCardExample duoCardExample;
}
