package com.arduino.telegrambot.statistick.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class DayStrikeTemplateDate {
    private String dayName;
    private Boolean isCompleted;
}
