package com.arduino.telegrambot.anki.model;

import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.List;


public record AnkiCurrentCard(
        long cardId,
        String deckName,
        String question,
        String answer,
        List <String> tags,
        List<Integer> buttons
) {
}
