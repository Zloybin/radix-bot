package com.arduino.telegrambot.feature.anki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddNoteParams {
    private final Note note;
}
