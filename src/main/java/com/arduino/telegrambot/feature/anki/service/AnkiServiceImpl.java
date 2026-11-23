package com.arduino.telegrambot.feature.anki.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.client.AnkiConnectClient;
import com.arduino.telegrambot.feature.anki.entity.AnkiDeckDailyInfo;
import com.arduino.telegrambot.feature.anki.mapper.AnkiResponseMapper;
import com.arduino.telegrambot.feature.anki.model.*;
import com.arduino.telegrambot.feature.anki.model.add.Note;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
import com.arduino.telegrambot.update.repository.AnkiDeckDailyInfoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Setter
@Getter
public class AnkiServiceImpl implements AnkiService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String SPLITTER = "::";
    public static final String RATED_TEMPLATE = "rated:%d";
    public static final String ADDED_TEMPLATE = "added:%d";
    public static final String INTRODUCED_TEMPLATE = "introduced:%d";
    public static final String DECK_TEMPLATE = "deck:\"%s\"";

    @Autowired
    private final AnkiConnectClient ankiClient;

    @Autowired
    private AnkiResponseMapper ankiResponseMapper;

    @Autowired
    private AnkiDeckDailyInfoRepository ankiDeckDailyInfoRepository;

    //Version
    @Override
    public Mono<Integer> getVersion() {
        return ankiClient.version();
    }

    // Decks
    @Override
    public Mono<List<String>> getDeckNames() {
        return ankiClient.getDeckNames()
                .map(ankiResponseMapper::toDeckName);
    }

    @Override
    public Mono<List<String>> getFilteredDeckNames() {
        return getDeckNames()
                .map(list -> list
                        .stream()
                        .filter(deckName -> !AnkiUtility.EXCLUDED_DECKS.contains(deckName))
                        .toList()
                );
    }

    @Override
    public Mono<List<String>> getFilteredChildDeckNames() {
        return getFilteredDeckNames().map(list -> list.stream()
                .map(deckName -> deckName.contains(SPLITTER)
                        ? deckName.split(SPLITTER)[1]
                        : deckName)
                .toList()
        );
    }

    @Override
    public String normalizeDeckName(String deckName) {
        if (deckName.contains(SPLITTER)) {
            String[] split = deckName.split(SPLITTER);
            return split[1];
        } else {
            return deckName;
        }
    }

    //Completion
    @Override
    public Mono<Boolean> startDeckReview(String deckName) {
        return ankiClient.guiDeckReview(deckName).map(JsonNode::asBoolean);
    }

    @Override
    public Mono<AnkiCurrentCard> getCurrentCardInfo() {
        return ankiClient.getCurrentCard().flatMap(currentCardJson -> {
            long cardId = currentCardJson.get("cardId").asLong();
            return getCardInfoById(cardId).map(ankiCurrentCard -> {
                ankiCurrentCard.setButtons(readIntegerList(currentCardJson.path("buttons")));
                        return ankiCurrentCard;
            });
        });
    }

    @Override
    public Mono<AnkiCurrentCard> getCardInfoById(long cardId) {
        return ankiClient.cardsInfo(List.of(cardId))
                .map(ankiResponseMapper::toAnkiCurrentCard);
    }

    @Override
    public Mono<Boolean> showAnswer() {
        return ankiClient.showAnswer().map(JsonNode::asBoolean);
    }

    @Override
    public Mono<Boolean> answerCard(int ease) {
        validateEase(ease);
        return ankiClient.answerCard(ease).map(JsonNode::asBoolean);
    }

    //Statistics
    @Override
    public Mono<Map<String, AnkiDeckStats>> getAnkiDecksStatisticsMap(List<String> deckNames) {
        return ankiClient.getDecksStatistics(deckNames)
                .map(ankiResponseMapper::toDeckStatisticsMap);
    }

    @Override
    public Mono<AnkiDeckStats> getAnkiDeckStatistics(String deckName) {
        return getAnkiDecksStatisticsMap(List.of(deckName))
                .map(deckNames -> deckNames.get(deckName));
    }

    @Override
    public Mono<Integer> getNumCardsReviewedToday() {
        return ankiClient.getNumCardsReviewedToday().map(JsonNode::asInt);
    }

    @Override
    public Mono<Integer> getDeckStrikeValue(String deck) {
        return ankiClient.cardReviews(deck, 0L)
                .flatMapMany(Flux::fromIterable)
                .map(review -> review.get(0).asLong())
                .map(this::toLocalDate)
                .distinct()
                .collectSortedList(Comparator.reverseOrder())
                .map(this::calculateDeckStrikeValue);
    }

    @Override
    public Mono<Integer> getActualWeekReviewedCardsCount(int days) {
        String query = String.format(RATED_TEMPLATE, days);
        return ankiClient.findNotes(query).map(ankiResponseMapper::toCardIdList).map(List::size);
    }

    @Override
    public Mono<Integer> getActualWeekAddedNotesCount(int days) {
        String query = String.format(ADDED_TEMPLATE, days);
        return ankiClient.findNotes(query).map(ankiResponseMapper::toCardIdList).map(List::size);
    }

    @Override
    public Mono<Integer> getAddedNotesCount(String deckName) {
        String query = String.format(DECK_TEMPLATE, deckName) + " " + String.format(ADDED_TEMPLATE, 1);
        return ankiClient.findNotes(query).map(ankiResponseMapper::mapFindNotes).map(List::size);
    }

    @Override
    public Mono<Integer> getReviewedCardsCount(String deckName) {
        String query = String.format(DECK_TEMPLATE, deckName) + " " + String.format(RATED_TEMPLATE, 1);
        return ankiClient.findNotes(query).map(ankiResponseMapper::toCardIdList).map(List::size);
    }

    @Override
    public Mono<Integer> getNewCardsLearnCount(String deckName) {
        String query = String.format(DECK_TEMPLATE, deckName) + " " + String.format(INTRODUCED_TEMPLATE, 1);
        return ankiClient.findNotes(query).map(ankiResponseMapper::toCardIdList).map(List::size);
    }


    //Management
    @Override
    public Mono<Long> saveNote(Note note) {
        return ankiClient.addNote(note).map(JsonNode::asLong);
    }

    @Override
    public Mono<List<Long>> saveNotes(List<Note> notes) {
        return ankiClient.addNotes(notes).map(result -> {
            List<Long> ids = new ArrayList<>();
            result.forEach(node -> ids.add(node.isNull() ? null : node.asLong()));
            return ids;
        });
    }

    @Override
    public Mono<Boolean> markCard(long cardId, int flag) {
        return ankiClient.setSpecificValueOfCard(cardId, flag).map(this::parseSetValueResult)
                .flatMap(success -> ankiClient.reloadCollection()
                        .thenReturn(success));
    }

    @Override
    public Mono<Boolean> deleteCard(long cardId) {
        return ankiClient.suspendCards(List.of(cardId)).map(JsonNode::asBoolean);
    }

    //DuoCards

    @Override
    public Mono<List<Long>> getCardIdsFromDeck(String deckName) {
        String query = String.format(DECK_TEMPLATE, deckName);
        return ankiClient.findCards(query).map(ankiResponseMapper::toCardIdList);
    }

    @Override
    public List<AnkiDeckDailyInfo> findAnkiDeckDailyInfoListUserByDate(User user, LocalDate date) {
        return ankiDeckDailyInfoRepository.findAllByUserAndAnkiDeckDailyInfoId_Date(user, date);
    }

//    ----------

    private long toEpochMilli(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    private LocalDate toLocalDate(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void validateEase(int ease) {

        if (ease < 1 || ease > 4) {
            throw new IllegalArgumentException(
                    "Ease должен быть от 1 до 4"
            );
        }
    }

    private int calculateDeckStrikeValue(List<LocalDate> datesDesc) {
        if(datesDesc == null || datesDesc.isEmpty()){
            return 0;
        }

        LocalDate mostRecent = datesDesc.get(0);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        if (datesDesc.isEmpty() || mostRecent.isBefore(yesterday)) {

            return 0;

        } else {

            for (int i = 0; i < datesDesc.size(); i++) {
                var actualDate = datesDesc.get(i);
                var nextExpectedDate = actualDate.minusDays(1);
                var nextActualDate = datesDesc.get(i + 1);
                if (!nextActualDate.isEqual(nextExpectedDate)) {
                    return (int) ChronoUnit.DAYS.between(actualDate, mostRecent) + 1;
                }
            }

            return datesDesc.size();
        }
    }

//    private int calculateStreak(LocalDate datesDesc) {
//        LocalDate today = LocalDate.now();


//
//        long lastReviewed = expected.atStartOfDay(ZoneId.systemDefault())
//                .plusDays(1L)
//                .toInstant().toEpochMilli();
//
//        // если сегодня ещё не проходили карточки — начинаем отсчёт со вчерашнего дня,
//        // иначе стрик сразу обнулится, хотя пользователь ещё может успеть позаниматься сегодня
//        if (!datesDesc.get(0).equals(expected)) {
//            expected = expected.minusDays(1);
//            lastReviewed = expected
//                    .atStartOfDay(ZoneId.systemDefault())
//                    .plusDays(1L)
//                    .toInstant()
//                    .toEpochMilli();
//        }
//
//        int streak = 0;
//        for (LocalDate date : datesDesc) {
//            if (date.equals(expected)) {
//                streak++;
//                expected = expected.minusDays(1);
//            } else if (date.isBefore(expected)) {
//                // нашли пропуск — дальше список можно отбрасывать
//                break;
//            }
//        }
//
//        if (streak == 0) {
//
//        }
//
//        return streak;
//    }

    private Boolean parseSetValueResult(JsonNode root) {
        if (root == null || !root.isArray() || root.isEmpty()) {
            System.out.println("***ANKI setSpecificValueOfCard: empty or unexpected response: " + root);
            return false;
        }

        boolean allSucceeded = true;

        for (JsonNode keyResult : root) {
            if (keyResult.isArray()) {
                // Формат при ошибке: [false, "error message"]
                boolean success = keyResult.get(0).asBoolean();
                if (!success) {
                    String errorMessage = keyResult.size() > 1 ? keyResult.get(1).asText() : "unknown error";
                    System.out.println("***ANKI setSpecificValueOfCard error: " + errorMessage);
                }
                allSucceeded &= success;
            } else {
                // Формат при успехе: true / false напрямую
                allSucceeded &= keyResult.asBoolean();
            }
        }

        return allSucceeded;
    }

    private List<Integer> readIntegerList(JsonNode node) {

        List<Integer> result = new ArrayList<>();

        node.forEach(element ->
                result.add(element.asInt())
        );

        return result;
    }


//    -------------

//    private byte[] decodeBase64(JsonNode response) {
//
//        if (response.isMissingNode() || response.isNull()) {
//            throw new IllegalStateException(
//                    "retrieveMediaFile returned no response"
//            );
//        }
//
//        if (!response.isTextual()) {
//            throw new IllegalStateException(
//                    "retrieveMediaFile response is not Base64 string"
//            );
//        }
//
//        try {
//            return Base64.getDecoder().decode(response.asText());
//
//        } catch (IllegalArgumentException e) {
//            throw new IllegalStateException(
//                    "Invalid Base64 returned by AnkiConnect",
//                    e
//            );
//        }
//    }
//
//    private Long extractCardId(JsonNode response) {
//        JsonNode cardIdNode = response
//                .path("cardId");
//
//        if (cardIdNode.isMissingNode() || cardIdNode.isNull()) {
//            throw new IllegalStateException(
//                    "guiCurrentCard did not return cardId"
//            );
//        }
//
//        return cardIdNode.asLong();
//    }
//
//    private List<CardInfo> parseCardInfo(String json) {
//        JsonNode root;
//
//        try {
//            root = MAPPER.readTree(json);
//
//            JsonNode resultNode = root;
//
//            if (root.isObject()) {
//                JsonNode error = root.get("error");
//
//                if (error != null && !error.isNull()) {
//                    throw new IllegalStateException(
//                            "AnkiConnect error: " + error.asText()
//                    );
//                }
//
//                resultNode = root.get("result");
//            }
//
//            if (resultNode == null || !resultNode.isArray()) {
//                throw new IllegalStateException(
//                        "Unexpected cardsInfo response: " + json
//                );
//            }
//
//            List<CardInfo> cards = MAPPER.convertValue(
//                    resultNode,
//                    CARD_LIST
//            );
//
//            // Игнорируем пустые объекты {}, возвращаемые для несуществующих ID
//            return cards.stream()
//                    .filter(card -> card.getCardId() != 0)
//                    .toList();
//
//        } catch (JsonProcessingException e) {
//            throw new IllegalStateException(
//                    "Failed to parse cardsInfo response: " + json, e
//            );
//        }
//    }
//
//    private List<Integer> readIntegerList(JsonNode node) {
//
//        List<Integer> result = new ArrayList<>();
//
//        node.forEach(element ->
//                result.add(element.asInt())
//        );
//
//        return result;
//    }
//
//    private Integer extractFlag(JsonNode cardsInfoResult) {
//        if (cardsInfoResult == null || !cardsInfoResult.isArray() || cardsInfoResult.isEmpty()) {
//            return 0;
//        }
//
//        return cardsInfoResult.get(0).path("flags").asInt(0);
//    }


//    ----------

//    @Override
//    public List<AnkiDeckStrikeInfo> refreshStrikeStats(List<AnkiDeckStrikeInfo> ankiDeckStrikeInfos) {
//
//        var refreshedStrikeInfos = new ArrayList<AnkiDeckStrikeInfo>();
//
//        for (AnkiDeckStrikeInfo ankiDeckStrikeInfo : ankiDeckStrikeInfos) {
//
//            long lastReviewedDate = ankiDeckStrikeInfo.getLastReviewedDate();
//
//            if (isReviewedAndRefreshedToday(lastReviewedDate) || isNotReviewedAndNotRefreshedToday(lastReviewedDate)) {
//
//                refreshedStrikeInfos.add(ankiDeckStrikeInfo);
//
//            } else if (isTodayReviewedButNotRefreshed(ankiDeckStrikeInfo.getDeckName(), ankiDeckStrikeInfo.getLastReviewedDate())) {
//
//                var refreshedDeckStrikeInfo = AnkiDeckStrikeInfo.builder()
//                        .id(ankiDeckStrikeInfo.getId())
//                        .user(ankiDeckStrikeInfo.getUser())
//                        .deckName(ankiDeckStrikeInfo.getDeckName())
//                        .strikeCount(ankiDeckStrikeInfo.getStrikeCount() + 1)
//                        .lastReviewedDate(LocalDate.now()
//                                .atStartOfDay(ZoneId.systemDefault())
//                                .plusDays(1)
//                                .plusHours(4)
//                                .toInstant()
//                                .toEpochMilli())
//                        .build();
//
//                refreshedStrikeInfos.add(refreshedDeckStrikeInfo);
//            } else if (isStreakBroken(lastReviewedDate)) {
//                var refreshedDeckStrikeInfo = AnkiDeckStrikeInfo.builder()
//                        .id(ankiDeckStrikeInfo.getId())
//                        .user(ankiDeckStrikeInfo.getUser())
//                        .deckName(ankiDeckStrikeInfo.getDeckName())
//                        .strikeCount(0)
//                        .lastReviewedDate(0L)
//                        .build();
//                refreshedStrikeInfos.add(refreshedDeckStrikeInfo);
//            }
//        }
//
//        return refreshedStrikeInfos;
//    }
//
//    @Override
//    private boolean isReviewedAndRefreshedToday(Long lastReviewedCardDate) {
//        ZoneId zone = ZoneId.systemDefault();
//        long endOfDay = LocalDate.now().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli();
//        return lastReviewedCardDate == endOfDay;
//    }
//
//    @Override
//    private boolean isNotReviewedAndNotRefreshedToday(Long lastReviewedCardDate) {
//
//        ZoneId zone = ZoneId.systemDefault();
//        long startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay(zone).plusHours(4).toInstant().toEpochMilli();
//        long endOfYesterday = LocalDate.now().atStartOfDay(zone).plusHours(4).toInstant().toEpochMilli();
//
//        return lastReviewedCardDate >= startOfYesterday && lastReviewedCardDate <= endOfYesterday;
//    }
//
//    @Override
//    private boolean isTodayReviewedButNotRefreshed(String deckName, long lastReviewedCardDate) {
//        Long lastCardReviewTime = ankiClient.lastReviewTime(deckName).block();
//
//        ZoneId zone = ZoneId.systemDefault();
//        long startOfDay = LocalDate.now().atStartOfDay(zone).toInstant().toEpochMilli();
//        long endOfDay = LocalDate.now().plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli();
//
//        return lastReviewedCardDate < lastCardReviewTime && lastCardReviewTime >= startOfDay && lastCardReviewTime < endOfDay;
//    }
//
//    @Override
//    private boolean isStreakBroken(long lastReviewedCardDate) {
//
//        ZoneId zone = ZoneId.systemDefault();
//        long startOfYesterday = LocalDate.now().minusDays(1).atStartOfDay(zone).plusHours(4).toInstant().toEpochMilli();
//
//        return lastReviewedCardDate < startOfYesterday;
//    }
//
//    @Override
//    public List<DeckProgress> updateUserDeckStatus(List<String> decks, List<DeckProgress> deckProgress) {
//
//        var updatedDeckProgressList = new ArrayList<DeckProgress>();
//
//        for (String deck : decks) {
//            for (DeckProgress progress : deckProgress) {
//
//                if (progress.getDeckName().equals(deck)) {
//                    DeckStatus updatedDeckStatus = DeckStatus.NOT_STARTED;
//                    LocalDate updatedLocalDate;
//
//                    String fullDeckName;
//                    if(deck.contains("::")){
//                        String[] split = deck.split("::");
//                        fullDeckName = split[1];
//                    }else{
//                        fullDeckName = deck;
//                    }
//
//                    var ankiDeckStats = ankiClient.getDeckStats(List.of(fullDeckName)).block().get(fullDeckName);
//
//                    var lastCardReviewDate = getLastCardReviewDate(deck);
//                    var localDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
//                    var start = Instant.ofEpochMilli(localDate).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).minusDays(1).plusHours(4).toLocalDateTime();
//                    var end = Instant.ofEpochMilli(localDate).atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).plusHours(4).toLocalDateTime();
//
//                    if (lastCardReviewDate.isAfter(end)) {
//                        updatedLocalDate = lastCardReviewDate.toLocalDate();
//                        if (isDeckCompleted(ankiDeckStats)) {
//                            if (!DeckStatus.COMPLETED.equals(progress.getDeckStatus())) {
//                                updatedDeckStatus = DeckStatus.COMPLETED;
//                            }
//                        } else {
//                            updatedDeckStatus = DeckStatus.IN_PROGRESS;
//                        }
//
//                    } else if (lastCardReviewDate.isAfter(start) && lastCardReviewDate.isBefore(end)) {
//                        updatedLocalDate = end.toLocalDate();
//                        if (isDeckCompleted(ankiDeckStats)) {
//                            if (!DeckStatus.COMPLETED.equals(progress.getDeckStatus())) {
//                                updatedDeckStatus = DeckStatus.COMPLETED;
//                            }
//
//                        } else {
//                            updatedDeckStatus = DeckStatus.IN_PROGRESS;
//                        }
//                    } else {
//                        updatedLocalDate = end.toLocalDate();
//                        if (!DeckStatus.NOT_STARTED.equals(progress.getDeckStatus())) {
//                            updatedDeckStatus = DeckStatus.NOT_STARTED;
//                        }
//                    }
//
//                    DeckProgress updatedDeckProgress = DeckProgress.builder()
//                            .localDate(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).plusDays(1).plusHours(4).toInstant().toEpochMilli())
//                            .deckName(progress.getDeckName())
//                            .deckStatus(updatedDeckStatus)
//                            .localDate(updatedLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
//                            .build();
//
//                    updatedDeckProgressList.add(updatedDeckProgress);
//                }
//            }
//        }
//        return updatedDeckProgressList;
//    }
//
//    @Override
//    private LocalDateTime getLastCardReviewDate(String deck) {
//        Long lastCardReviewTime = ankiClient.lastReviewTime(deck).block();
//        if (lastCardReviewTime == null){
//            System.out.println(String.format("Deck %s не имеет lastReviewTime", deck));
//        }
//        return Instant.ofEpochMilli(lastCardReviewTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
//    }
//
//    @Override
//    private boolean isDeckCompleted(AnkiDeckDailyInfo ankiDeckDailyInfo) {
//        return ankiDeckDailyInfo.getLearnCount() == 0 && ankiDeckDailyInfo.getNewCount() == 0 && ankiDeckDailyInfo.getReviewCount() == 0;
//    }
}
