package com.arduino.telegrambot.feature.anki.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.entity.AnkiDeckDailyInfo;
import com.arduino.telegrambot.feature.anki.model.*;
import com.arduino.telegrambot.feature.anki.model.add.Note;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnkiService {

    //Version
    Mono<Integer> getVersion();

    // Decks
    Mono<List<String>> getDeckNames();
    Mono<List<String>> getFilteredDeckNames();
    Mono<List<String>> getFilteredChildDeckNames();
    String normalizeDeckName(String deckName);

    //Completion
    Mono<Boolean> startDeckReview(String deckName);
    Mono<AnkiCurrentCard> getCurrentCardInfo();
    Mono<AnkiCurrentCard> getCardInfoById(long cardId);
    Mono<Boolean> showAnswer();
    Mono<Boolean> answerCard(int ease);

    //Statistics
    Mono<Map<String, AnkiDeckStats>> getAnkiDecksStatisticsMap(List<String> deckNames);
    Mono<AnkiDeckStats> getAnkiDeckStatistics(String deckName);
    Mono<Integer> getNumCardsReviewedToday();
    Mono<Integer> getDeckStrikeValue(String deck);
    Mono<Integer> getActualWeekAddedNotesCount(int days);
    Mono<Integer> getActualWeekReviewedCardsCount(int days);
    Mono<Integer> getAddedNotesCount(String deckName);
    Mono<Integer> getReviewedCardsCount(String deckName);
    Mono<Integer> getNewCardsLearnCount(String deckName);

    //Management
    Mono<Long> saveNote(Note note);
    Mono<List<Long>> saveNotes(List<Note> notes);
    Mono<Boolean> markCard(long cardId, int flag);
    Mono<Boolean> deleteCard(long cardId);

    //DuoCards
    Mono<List<Long>> getCardIdsFromDeck(String deckName);
    List<AnkiDeckDailyInfo> findAnkiDeckDailyInfoListUserByDate(User user, LocalDate date);
}
