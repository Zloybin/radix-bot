package com.arduino.telegrambot.anki.model;

import java.util.List;

public record AnkiCurrentCard(
        long cardId,
        String question,
        String answer,
        List<Integer> buttons,
        List<String> nextReviews
) {
}
