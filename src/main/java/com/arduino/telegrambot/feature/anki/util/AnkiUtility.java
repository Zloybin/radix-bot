package com.arduino.telegrambot.feature.anki.util;

import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;

import java.util.List;
import java.util.Set;


public class AnkiUtility {
    public static final List<String> EXCLUDED_DECKS = List.of("По умолчанию", "Completed Deck", "Deutsch", "Elektrotechnika", "SPS", "TMM");


    public static String getFlagView(Integer flagValue) {
        return switch (flagValue){
            case 0 -> "";
            case 1 -> "🚩";
            default -> throw new AnkiConnectException("Неопределенное значение флага.");
        };
    }
}
