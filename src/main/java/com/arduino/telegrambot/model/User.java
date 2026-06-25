package com.arduino.telegrambot.model;

import com.arduino.telegrambot.enummeration.UserState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class User {
    long id;
    UserState state;
    String task;
}
