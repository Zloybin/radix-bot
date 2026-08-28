package com.arduino.telegrambot.anki;

public record AnkiDeckStats(
        long deckId,
        String name,
        int newCount,
        int learnCount,
        int reviewCount,
        int totalInDeck
) {
}
