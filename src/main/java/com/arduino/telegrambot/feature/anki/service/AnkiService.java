package com.arduino.telegrambot.feature.anki.service;

import com.arduino.telegrambot.entity.DeckProgress;
import com.arduino.telegrambot.entity.DeckStrikeInfo;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface AnkiService {

    //deck
    Mono<List<String>> getDecks();


    //card
    Mono<Boolean> startStudy(String deckName);
    Mono<AnkiCurrentCard> getCurrentCard();
    Mono<Boolean> showAnswer();
    Mono<Boolean> answerCard(int ease);
    Mono<AnkiCurrentCard> answerAndGetNextCard(int ease);
    Mono<Boolean> deleteCard(long cardId);
    Mono<Boolean> markCard(long cardId);

    Mono<Integer> getVersion();


    //stats
    Mono<Integer> getNumCardsReviewedToday();
    Mono<Map<String, AnkiDeckStats>> getDecksStats(List<String> deckNames);
    Mono<AnkiDeckStats> getDeckStats(String deckName);
    List<DeckProgress> updateUserDeckStatus(List<String> decks, List<DeckProgress> deckProgress);
    Integer getDecksStrikeStat(String deckName, long lastReviewedDate);
    List<DeckStrikeInfo> initialStrikeStats(List<String> deckNames);
    List<DeckStrikeInfo> refreshStrikeStats(List<DeckStrikeInfo> deckStrikeInfos);

    byte[] getVideo();
}
