package com.arduino.telegrambot.statistick.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Builder
@Setter @Getter
public class DayDeckTemplateDate {
    String deckName;
    int reviewedCardsCount;
    int uncompletedCardsCount;
    boolean isSuccesses;
}
