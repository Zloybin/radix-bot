package com.arduino.telegrambot.feature.anki.client;

import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.model.InitStrikeStatDate;
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
    Mono<Map<String, AnkiDeckStats>> getDeckStats(List<String> deckNames);

    // Reviewer
    Mono<Boolean> startDeckReview(String deckName);
    Mono<AnkiCurrentCard> getCurrentCard();
    Mono<Boolean> deleteCard(long cardId);
    Mono<Boolean> showAnswer();
    Mono<Boolean> answerCard(int ease);
    Mono<byte[]> getCurrentCardVideo();

    Mono<Boolean> setSpecificValueOfCard(long cardId);

    //Statistic
    Mono<Integer> getNumCardsReviewedToday();
    Mono<Integer> cardReviews(String deck, long now);
    Mono<InitStrikeStatDate> initialStrikeStat(String deck);
    Mono<Long> lastReviewTime(String deck);
}