package com.arduino.telegrambot.feature.anki.model;

import com.arduino.telegrambot.feature.duocards.deck.enummeration.AnkiTemplate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class AnkiCurrentCard {
        private long cardId;
        private String deckName;
        private String question;
        private String answer;
        private String example;
        private List<String> tags;
        private List<Integer> buttons;
        private AnkiTemplate ankiTemplate;
        private Integer flag;
}

