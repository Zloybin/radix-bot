package com.arduino.telegrambot.feature.anki.model;

public record AnkiDeckStats(

        int newCount,
        int learnCount,
        int reviewCount,
        int totalInDeck
) {
}
