package com.arduino.telegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "callbacks")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Callback {

    @Id
    private Long id;

    @Column
    private String handler;

    @Column
    private String request;
}
