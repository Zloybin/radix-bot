package com.arduino.telegrambot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TaskResult {
    private boolean result;
    private String userAnswer;
    private String rightAnswer;
}
