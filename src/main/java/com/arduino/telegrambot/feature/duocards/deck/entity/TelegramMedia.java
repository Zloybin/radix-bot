package com.arduino.telegrambot.feature.duocards.deck.entity;

import com.arduino.telegrambot.telegram.enummeration.TelegramMediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "medias")
@NoArgsConstructor @AllArgsConstructor
@Builder @Setter @Getter
@EqualsAndHashCode @ToString
public class TelegramMedia {

    @Id
    private String key;

    @Column(nullable = false)
    private TelegramMediaType mediaType;

    @Column(nullable = false)
    private String fileId;
}
