package com.arduino.telegrambot.entity;

import com.arduino.telegrambot.enummeration.DeckStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "deck_progress")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeckProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private DeckStatus deckStatus;

    @Column
    private String deckName;

    @Column
    private long localDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
