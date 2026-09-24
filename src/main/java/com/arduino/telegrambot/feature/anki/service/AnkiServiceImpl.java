package com.arduino.telegrambot.feature.anki.service;

import com.arduino.telegrambot.entity.DeckProgress;
import com.arduino.telegrambot.entity.DeckStrikeInfo;
import com.arduino.telegrambot.enummeration.DeckStatus;
import com.arduino.telegrambot.feature.anki.client.AnkiConnectClient;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.model.InitStrikeStatDate;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnkiServiceImpl implements AnkiService{

    private final AnkiConnectClient ankiClient;

    public AnkiServiceImpl(AnkiConnectClient ankiClient) {
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
    public Mono<Map<String, AnkiDeckStats>> getDecksStats(List<String> deckNames) {
        return ankiClient.getDeckStats(deckNames);
    }

    public Mono<AnkiDeckStats> getDeckStats(
            String deckName
    ) {
        return ankiClient.getDeckStats(List.of(deckName)).map(deckNames -> deckNames.get(deckName));
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

    public Mono<Boolean> deleteCard(long cardId) {
        return ankiClient.deleteCard(cardId);
    }

    @Override
    public Mono<Boolean> markCard(long cardId) {
        return ankiClient.setSpecificValueOfCard(cardId);
    }

    public Mono<Integer> getNumCardsReviewedToday() {
        return ankiClient.getNumCardsReviewedToday();
    }

    private void validateEase(int ease) {

        if (ease < 1 || ease > 4) {
            throw new IllegalArgumentException(
                    "Ease должен быть от 1 до 4"
            );
        }
    }

    public byte[] getVideo() {
        return ankiClient.getCurrentCardVideo().block();
    }

    public Integer getDecksStrikeStat(String deckName, long lastReviewedDate) {
        return ankiClient.cardReviews(deckName, lastReviewedDate).block();
    }

    public List<DeckStrikeInfo> initialStrikeStats(List<String> deckNames) {

        var deckStrikeInfos = new ArrayList<DeckStrikeInfo>();
        for (String deckName : deckNames) {
            if (!AnkiUtility.EXCLUDED_DECKS.contains(deckName)) {
                InitStrikeStatDate initStrikeStatDate = ankiClient.initialStrikeStat(deckName).block();
                DeckStrikeInfo deckStrikeInfo = DeckStrikeInfo.builder()
                        .deckName(deckName)
                        .strikeCount(initStrikeStatDate.getStrikeCount())
                        .lastReviewedDate(initStrikeStatDate.getLastReviewDate())
                        .build();

                deckStrikeInfos.add(deckStrikeInfo);
            }
        }
        return deckStrikeInfos;
    }

    public List<DeckStrikeInfo> refreshStrikeStats(List<DeckStrikeInfo> deckStrikeInfos) {

        var refreshedStrikeInfos = new ArrayList<DeckStrikeInfo>();

        for (DeckStrikeInfo deckStrikeInfo : deckStrikeInfos) {

            long lastReviewedDate = deckStrikeInfo.getLastReviewedDate();

            if (isReviewedAndRefreshedToday(lastReviewedDate) || isNotReviewedAndNotRefreshedToday(lastReviewedDate)) {

                refreshedStrikeInfos.add(deckStrikeInfo);

            } else if (isTodayReviewedButNotRefreshed(deckStrikeInfo.getDeckName(), deckStrikeInfo.getLastReviewedDate())) {

                var refreshedDeckStrikeInfo = DeckStrikeInfo.builder()
                        .id(deckStrikeInfo.getId())
                        .user(deckStrikeInfo.getUser())
                        .deckName(deckStrikeInfo.getDeckName())
                        .strikeCount(deckStrikeInfo.getStrikeCount() + 1)
                        .lastReviewedDate(LocalDate.now()
                                .atStartOfDay(ZoneId.systemDefault())
                                .plusDays(1)
                                .plusHours(4)
                                .toInstant()
                                .toEpochMilli())
                        .build();

                refreshedStrikeInfos.add(refreshedDeckStrikeInfo);
            } else if (isStreakBroken(lastReviewedDate)) {
                var refreshedDeckStrikeInfo = DeckStrikeInfo.builder()
                        .id(deckStrikeInfo.getId())
                        .user(deckStrikeInfo.getUser())
                        .deckName(deckStrikeInfo.getDeckName())
                        .strikeCount(0)
                        .lastReviewedDate(0L)
                        .build();
                refreshedStrikeInfos.add(refreshedDeckStrikeInfo);
            }
        }

        return refreshedStrikeInfos;
    }


    private boolean isReviewedAndRefreshedToday(Long lastReviewedCardDate) {
        ZoneId zone = ZoneId.systemDefault();
        long endOfDay = LocalDate.now().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli();
        return lastReviewedCardDate == endOfDay;
    }

    private boolean isNotReviewedAndNotRefreshedToday(Long lastReviewedCardDate) {

        ZoneId zone = ZoneId.systemDefault();
        long startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay(zone).plusHours(4).toInstant().toEpochMilli();
        long endOfYesterday = LocalDate.now().atStartOfDay(zone).plusHours(4).toInstant().toEpochMilli();

        return lastReviewedCardDate >= startOfYesterday && lastReviewedCardDate <= endOfYesterday;
    }

    private boolean isTodayReviewedButNotRefreshed(String deckName, long lastReviewedCardDate) {
        Long lastCardReviewTime = ankiClient.lastReviewTime(deckName).block();

        ZoneId zone = ZoneId.systemDefault();
        long startOfDay = LocalDate.now().atStartOfDay(zone).toInstant().toEpochMilli();
        long endOfDay = LocalDate.now().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli();

        return lastReviewedCardDate < lastCardReviewTime && lastCardReviewTime >= startOfDay && lastCardReviewTime < endOfDay;
    }

    private boolean isStreakBroken(long lastReviewedCardDate) {

        ZoneId zone = ZoneId.systemDefault();
        long startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay(zone).plusHours(4).toInstant().toEpochMilli();

        return lastReviewedCardDate < startOfYesterday;
    }

    public List<DeckProgress> updateUserDeckStatus(List<String> decks, List<DeckProgress> deckProgress) {

        var updatedDeckProgressList = new ArrayList<DeckProgress>();

        for (String deck : decks) {
            for (DeckProgress progress : deckProgress) {

                if (progress.getDeckName().equals(deck)) {
                    DeckStatus updatedDeckStatus = DeckStatus.NOT_STARTED;
                    LocalDate updatedLocalDate;

                    var ankiDeckStats = ankiClient.getDeckStats(List.of(deck)).block().get(deck);

                    var lastCardReviewDate = getLastCardReviewDate(deck);
                    var localDate = progress.getLocalDate();
                    var start = Instant.ofEpochMilli(localDate).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).minusDays(1).plusHours(4).toLocalDateTime();
                    var end = Instant.ofEpochMilli(localDate).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusHours(4).toLocalDateTime();

                    if (lastCardReviewDate.isAfter(end)) {
                        updatedLocalDate = lastCardReviewDate.toLocalDate();
                        if (isDeckCompleted(ankiDeckStats)) {
                            if (!DeckStatus.COMPLETED.equals(progress.getDeckStatus())) {
                                updatedDeckStatus = DeckStatus.COMPLETED;
                            }
                        } else {
                            updatedDeckStatus = DeckStatus.IN_PROGRESS;
                        }

                    } else if (lastCardReviewDate.isAfter(start) && lastCardReviewDate.isBefore(end)) {
                        updatedLocalDate = end.toLocalDate();
                        if (isDeckCompleted(ankiDeckStats)) {
                            if (!DeckStatus.COMPLETED.equals(progress.getDeckStatus())) {
                                updatedDeckStatus = DeckStatus.COMPLETED;
                            }

                        } else {
                            updatedDeckStatus = DeckStatus.IN_PROGRESS;
                        }
                    } else {
                        updatedLocalDate = end.toLocalDate();
                        if (!DeckStatus.NOT_STARTED.equals(progress.getDeckStatus())) {
                            updatedDeckStatus = DeckStatus.NOT_STARTED;
                        }
                    }

                    DeckProgress updatedDeckProgress = DeckProgress.builder()
                            .user(progress.getUser())
                            .localDate(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusDays(1).plusHours(4).toInstant().toEpochMilli())
                            .deckName(progress.getDeckName())
                            .deckStatus(updatedDeckStatus)
                            .localDate(updatedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
                            .build();

                    updatedDeckProgressList.add(updatedDeckProgress);
                }
            }
        }
        return updatedDeckProgressList;
    }

    private LocalDateTime getLastCardReviewDate(String deck) {
        Long lastCardReviewTime = ankiClient.lastReviewTime(deck).block();
        return Instant.ofEpochMilli(lastCardReviewTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private boolean isDeckCompleted(AnkiDeckStats ankiDeckStats) {
        return ankiDeckStats.learnCount() == 0 && ankiDeckStats.newCount() == 0 && ankiDeckStats.reviewCount() == 0;
    }
}
