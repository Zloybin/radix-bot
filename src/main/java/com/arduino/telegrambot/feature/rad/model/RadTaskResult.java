package com.arduino.telegrambot.feature.rad.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class RadTaskResult {
    private boolean result;
    private String userAnswer;
    private String rightAnswer;
}
