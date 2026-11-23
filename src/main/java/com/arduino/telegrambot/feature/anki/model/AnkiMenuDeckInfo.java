package com.arduino.telegrambot.feature.anki.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class AnkiMenuDeckInfo {
    private String deckName;
    private int reviewedCardCount;
    private int uncompletedCardCount;
    private int deckStrike;
}
