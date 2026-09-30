package com.arduino.telegrambot.feature.duocards.model;

public record DuoCard(
        String id,
        String deckId,
        String front,
        String back,
        String hint,
        Integer knownCount,
        Integer failCount,
        Boolean flipped,
        String knownAt,
        String knownUntil,
        String loopedAt
) {
}