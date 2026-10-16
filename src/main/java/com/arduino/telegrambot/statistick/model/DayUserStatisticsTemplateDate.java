package com.arduino.telegrambot.statistick.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class DayUserStatisticsTemplateDate {
    private int completedPhysTaskCount;
    private boolean isPhysTaskCompleted;
    private int completedDuoCardsSentenceTaskCount;
    private boolean isDuoCardsSentenceTaskCompleted;
    private List<DayStrikeTemplateDate> strikeListTemplateDate;
    private List<DayDeckTemplateDate> deckListTemplateDate;
}
