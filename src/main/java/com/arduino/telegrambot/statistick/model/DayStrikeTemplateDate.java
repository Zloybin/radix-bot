package com.arduino.telegrambot.statistick.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter @Getter
public class DayStrikeTemplateDate {
    String dayName;
    Boolean isCompleted;
}
