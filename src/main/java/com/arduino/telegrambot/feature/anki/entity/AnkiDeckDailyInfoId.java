package com.arduino.telegrambot.feature.anki.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class AnkiDeckDailyInfoId implements Serializable {

    private Long userId;
    private String deckName;
    private LocalDate date;
}
