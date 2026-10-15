package com.arduino.telegrambot.model;
import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Builder
@Setter
@Getter
@AllArgsConstructor @NoArgsConstructor
public class DayStrikeInfo {
    private boolean isAnkiDecksCompleted;
    private int completedPhysTaskCount;
    private int completedDuoCardsSentenceTaskCount;

    public boolean isDaySuccesses(){
        return isAnkiDecksCompleted
                && completedPhysTaskCount >= 5
                && completedDuoCardsSentenceTaskCount >= 5;
    }
}
