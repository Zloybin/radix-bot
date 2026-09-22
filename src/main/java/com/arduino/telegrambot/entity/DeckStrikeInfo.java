package com.arduino.telegrambot.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "strikes")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeckStrikeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String deckName;

    @Column
    private int strikeCount;

    @Column
    private long lastReviewedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

}
