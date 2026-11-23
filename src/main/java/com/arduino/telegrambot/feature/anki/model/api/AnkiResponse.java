package com.arduino.telegrambot.feature.anki.model.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor   // нужен Jackson для десериализации
public class AnkiResponse<R> {
    private R result;
    private String error;
}
