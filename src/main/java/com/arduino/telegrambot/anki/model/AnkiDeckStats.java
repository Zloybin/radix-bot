package com.arduino.telegrambot.anki.model;

public record AnkiDeckStats(

        int newCount,
        int learnCount,
        int reviewCount,
        int totalInDeck
) {
}
