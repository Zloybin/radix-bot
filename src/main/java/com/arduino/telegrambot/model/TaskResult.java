package com.arduino.telegrambot.model;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class TaskResult {
    private boolean result;
    private String userAnswer;
    private String rightAnswer;
}
