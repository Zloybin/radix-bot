package com.arduino.telegrambot.feature.anki.client;

import com.arduino.telegrambot.feature.anki.model.add.Note;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public interface AnkiConnectClient {

    // Version
    Mono<Integer> version();

    // Decks
    Mono<JsonNode> getDeckNames();

    //Completion
    Mono<JsonNode> guiDeckReview(String deckName);
    Mono<JsonNode> getCurrentCard();
    Mono<JsonNode> cardsInfo(List<Long> cardIds);
    Mono<JsonNode> showAnswer();
    Mono<JsonNode> answerCard(int ease);

    //Statistic
    Mono<JsonNode> getNumCardsReviewedToday();
    Mono<JsonNode> getDecksStatistics(List<String> deckNames);
    Mono<JsonNode> cardReviews(String deck, long start);
    Mono<JsonNode> findCards(String query);
    Mono<JsonNode> findNotes(String query);

    //Features
    Mono<JsonNode> setSpecificValueOfCard(long cardId, int flag);

    // Management
    Mono<JsonNode> addNote(Note note);
    Mono<JsonNode> addNotes(List<Note> notes);
    Mono<JsonNode> suspendCards(List<Long> cards);
    Mono<JsonNode> reloadCollection();
}