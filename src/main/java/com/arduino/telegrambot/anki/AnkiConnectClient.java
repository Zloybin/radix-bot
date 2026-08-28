package com.arduino.telegrambot.anki;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public interface AnkiConnectClient {

    // API
    Mono<Integer> version();

    // Decks
    Mono<List<String>> getDeckNames();

    Mono<Map<String, Long>> getDeckNamesAndIds();

    Mono<Boolean> startDeckReview(String deckName);

    Mono<Map<String, AnkiDeckStats>> getDeckStats(List<String> deckNames);

    // Reviewer
    Mono<AnkiCurrentCard> getCurrentCard();

    Mono<Boolean> startCardTimer();

    Mono<Boolean> showQuestion();

    Mono<Boolean> showAnswer();

    Mono<Boolean> answerCard(int ease);
}