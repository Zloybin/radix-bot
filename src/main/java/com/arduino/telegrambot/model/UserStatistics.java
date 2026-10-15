package com.arduino.telegrambot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Builder
@Setter
@Getter
public class UserStatistics {
    private int completedPhysTaskCount;
    private int completedDuoCardsSentenceTaskCount;
    private Map<LocalDate, Boolean> weekStrikeInfo;

    public boolean isPhysTasksComplete(){
        return completedPhysTaskCount >= 5;
    }

    public boolean isDuoCardsSentenceTaskComplete(){
        return completedDuoCardsSentenceTaskCount >= 5;
    }
}
