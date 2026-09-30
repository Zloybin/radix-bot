package com.arduino.telegrambot.feature.anki.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnkiRequest<T> {

    private static final int API_VERSION = 6;

    private final String action;
    private final int version;
    private final T params;   // null для действий без параметров (deckNames, modelNames...)

    public static <T> AnkiRequest<T> of(String action, T params) {
        return new AnkiRequest<>(action, API_VERSION, params);
    }
}
