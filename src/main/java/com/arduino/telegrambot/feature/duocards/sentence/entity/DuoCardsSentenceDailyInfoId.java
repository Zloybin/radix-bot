package com.arduino.telegrambot.feature.duocards.sentence.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class DuoCardsSentenceDailyInfoId implements Serializable {
    private Long userId;
    private LocalDate date;
}
