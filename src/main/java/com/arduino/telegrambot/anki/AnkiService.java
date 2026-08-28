package com.arduino.telegrambot.anki;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AnkiService {

    private final AnkiConnectClient ankiClient;

    public AnkiService(AnkiConnectClient ankiClient) {
        this.ankiClient = ankiClient;
    }

    /**
     * Проверяет доступность AnkiConnect.
     */
    public Mono<Integer> getVersion() {
        return ankiClient.version();
    }

    /**
     * Получает список всех колод.
     */
    public Mono<List<String>> getDecks() {
        return ankiClient.getDeckNames();
    }

    /**
     * Получает названия колод и их ID.
     */
    public Mono<Map<String, Long>> getDecksWithIds() {
        return ankiClient.getDeckNamesAndIds();
    }

    /**
     * Получает статистику указанных колод.
     */
    public Mono<Map<String, AnkiDeckStats>> getDeckStats(
            List<String> deckNames
    ) {
        return ankiClient.getDeckStats(deckNames);
    }

    /**
     * Открывает выбранную колоду в Reviewer Anki.
     */
    public Mono<Boolean> startStudy(String deckName) {
        return ankiClient.startDeckReview(deckName);
    }

    /**
     * Получает текущую карточку из Reviewer.
     */
    public Mono<AnkiCurrentCard> getCurrentCard() {
        return ankiClient.getCurrentCard();
    }

    /**
     * Запускает таймер карточки.
     */
    public Mono<Boolean> startCardTimer() {
        return ankiClient.startCardTimer();
    }

    /**
     * Показывает вопрос карточки.
     */
    public Mono<Boolean> showQuestion() {
        return ankiClient.showQuestion();
    }

    /**
     * Показывает ответ карточки.
     */
    public Mono<Boolean> showAnswer() {
        return ankiClient.showAnswer();
    }

    /**
     * Оценивает текущую карточку.
     *
     * @param ease 1 = Again
     *             2 = Hard
     *             3 = Good
     *             4 = Easy
     */
    public Mono<Boolean> answerCard(int ease) {
        validateEase(ease);

        return ankiClient.answerCard(ease);
    }

    /**
     * Оценивает текущую карточку и получает следующую.
     */
    public Mono<AnkiCurrentCard> answerAndGetNextCard(int ease) {
        validateEase(ease);

        return ankiClient.answerCard(ease)
                .flatMap(success -> {

                    if (!success) {
                        return Mono.error(
                                new IllegalStateException(
                                        "AnkiConnect не смог оценить карточку"
                                )
                        );
                    }

                    return ankiClient.getCurrentCard();
                });
    }

    private void validateEase(int ease) {

        if (ease < 1 || ease > 4) {
            throw new IllegalArgumentException(
                    "Ease должен быть от 1 до 4"
            );
        }
    }
}
