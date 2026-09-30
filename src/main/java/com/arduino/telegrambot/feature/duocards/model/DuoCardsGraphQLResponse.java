package com.arduino.telegrambot.feature.duocards.model;

public record DuoCardsGraphQLResponse(
        Data data
) {

    public record Data(
            CardCreate cardCreate
    ) {
    }

    public record CardCreate(
            DuoCard card,
            Deck deck
    ) {
    }

    public record Deck(
            String id
    ) {
    }
}