package com.arduino.telegrambot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserRequest {
    private Long chatId;
    private int messageId;
    private String request;
}
