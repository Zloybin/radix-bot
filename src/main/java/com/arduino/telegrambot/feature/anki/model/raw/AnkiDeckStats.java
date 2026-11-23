package com.arduino.telegrambot.feature.anki.model.raw;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnkiDeckStats {
        private int newCount;
        private int learnCount;
        private int reviewCount;
        private int totalInDeck;
}
