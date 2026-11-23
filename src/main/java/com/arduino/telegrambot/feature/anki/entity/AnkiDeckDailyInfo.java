package com.arduino.telegrambot.feature.anki.entity;

import com.arduino.telegrambot.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "anki_deck_daily_info")
@Getter
@Setter
@Builder
@ToString(exclude = "user")
@NoArgsConstructor
@AllArgsConstructor
public class AnkiDeckDailyInfo {

    @EmbeddedId
    private AnkiDeckDailyInfoId ankiDeckDailyInfoId;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int reviewedCardsCount;
    private int uncompletedCardsCount;
    private int addedNotesCount;
    private int newCardsLearnedCount;
}
