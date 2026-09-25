package com.arduino.telegrambot.feature.anki.model;

import com.arduino.telegrambot.enummeration.AnkiTemplate;

import java.util.List;


public record AnkiCurrentCard(
        long cardId,
        String deckName,
        String question,
        String answer,
        String example,
        List <String> tags,
        List<Integer> buttons,
        AnkiTemplate ankiTemplate
) {
}
