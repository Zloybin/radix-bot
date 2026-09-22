package com.arduino.telegrambot.feature.anki.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class InitStrikeStatDate {
    private int strikeCount;
    private long lastReviewDate;
}


