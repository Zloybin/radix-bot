package com.arduino.telegrambot.feature.anki.client;

import com.arduino.telegrambot.entity.DeckStrikeInfo;
import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.model.InitStrikeStatDate;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnkiConnectWebClient implements AnkiConnectClient {

    private static final int API_VERSION = 6;

    private final String ankiBaseUrl;

    private final WebClient webClient;


    public AnkiConnectWebClient(@Value("${anki.baseUrl}") String ankiBaseUrl) {
        this.ankiBaseUrl = ankiBaseUrl;
        ConnectionProvider provider = ConnectionProvider.newConnection();
        HttpClient httpClient = HttpClient.create(provider);
        this.webClient = WebClient.builder()
                .baseUrl(ankiBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)
                )
                .build();
    }

    @Override
    public Mono<List<String>> getDeckNames() {
        return invoke("deckNames", Map.of())
                .map(json -> {
                    List<String> decks = new ArrayList<>();
                    json.elements().forEachRemaining(
                            element -> decks.add(element.asText())
                    );
                    return decks;
                });
    }

    @Override
    public Mono<Map<String, Long>> getDeckNamesAndIds() {
        return invoke("deckNamesAndIds", Map.of())
                .map(json -> {
                    Map<String, Long> result = new java.util.HashMap<>();

                    json.fields().forEachRemaining(entry ->
                            result.put(
                                    entry.getKey(),
                                    entry.getValue().asLong()
                            )
                    );

                    return result;
                });
    }

    @Override
    public Mono<Map<String, AnkiDeckStats>> getDeckStats(
            List<String> deckNames
    ) {
        return invoke(
                "getDeckStats",
                Map.of("decks", deckNames)
        ).map(json -> {
            Map<String, AnkiDeckStats> result = new HashMap<>();

            json.fields().forEachRemaining(entry -> {

                AnkiDeckStats stats =
                        new AnkiDeckStats(
                                entry.getValue().get("new_count").asInt(),
                                entry.getValue().get("learn_count").asInt(),
                                entry.getValue().get("review_count").asInt(),
                                entry.getValue().get("total_in_deck").asInt()
                        );

                result.put(entry.getValue().get("name").asText(), stats);
            });

            return result;
        });
    }

    @Override
    public Mono<Boolean> startDeckReview(String deckName) {
        return invoke(
                "guiDeckReview",
                Map.of("name", deckName)
        ).map(JsonNode::asBoolean);
    }

    @Override
    public Mono<Boolean> setSpecificValueOfCard(long cardId) {
        return invoke(
                "setSpecificValueOfCard",
                Map.of("card", cardId, "keys", List.of("flags"), "newValues", List.of(1), "warning_check", true)
        ).doOnNext(jsonNode -> System.out.println(jsonNode))
                .map(JsonNode::asBoolean);
    }

    @Override
    public Mono<AnkiCurrentCard> getCurrentCard() {
        return invoke("guiCurrentCard", Map.of())
                .map(json -> {

                            var currentCard = new AnkiCurrentCard(
                                    json.get("cardId").asLong(),
                                    json.path("deckName").asText(),
                                    json.path("fields").path("Front").path("value").asText(),
                                    json.path("fields")
                                            .path("Back")
                                            .path("value")
                                            .asText(),
                                    json.path("fields")
                                            .path("Beispiel")
                                            .path("value")
                                            .asText(),
                                    Jsoup.parse(
                                                    json.path("fields").path("DisplayTags").path("value").asText()
                                            ).select(".tag")
                                            .eachText(),
                                    readIntegerList(json.get("buttons")));

                            System.out.println("***GET ANKI CARD:" + currentCard);
                            return currentCard;
                        }
                );
    }

    @Override
    public Mono<Boolean> deleteCard(long cardId) {
        return invoke("suspend", Map.of("cards", List.of(cardId)))
                .map(JsonNode::asBoolean);
    }

    @Override
    public Mono<Boolean> showAnswer() {
        return invoke("guiShowAnswer", Map.of())
                .map(JsonNode::asBoolean);
    }

    @Override
    public Mono<Integer> getNumCardsReviewedToday() {
        return invoke("getNumCardsReviewedToday", Map.of())
                .map(JsonNode::asInt);
    }

    @Override
    public Mono<Integer> cardReviews(String deck, long now) {
        return invoke("cardReviews", Map.of("deck", deck, "startID", now))
                .doOnNext(json -> System.out.println(json))
                .map(JsonNode::asInt);
    }

    @Override
    public Mono<Boolean> answerCard(int ease) {
        return invoke(
                "guiAnswerCard",
                Map.of("ease", ease)
        )
                .doOnNext(json ->
                        System.out.println("guiAnswerCard result: " + json)
                ).map(JsonNode::asBoolean);
    }

    @Override
    public Mono<byte[]> getCurrentCardVideo() {
        return invoke("guiCurrentCard", Map.of())
                .flatMap(cardResponse -> {
                    Long cardId = extractCardId(cardResponse);

                    return invoke(
                            "cardsInfo",
                            Map.of("cards", List.of(cardId))
                    );
                })
                .flatMap(cardInfoResponse -> {
                    String filename = extractVideoFilename(cardInfoResponse);

                    return invoke(
                            "retrieveMediaFile",
                            Map.of("filename", filename)
                    );
                })
                .map(this::decodeBase64);
    }

    @Override
    public Mono<Integer> version() {
        return invoke("version", Map.of())
                .map(JsonNode::asInt);
    }

    private Mono<JsonNode> invoke(
            String action,
            Map<String, Object> params
    ) {
        Map<String, Object> request = Map.of(
                "action", action,
                "version", API_VERSION,
                "params", params
        );

        System.out.println("ANKI REQUEST: " + request);

        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnNext(response ->
                        System.out.println("ANKI RESPONSE: " + response)
                )
                .flatMap(this::handleResponse);
    }

    private List<Integer> readIntegerList(JsonNode node) {

        List<Integer> result = new ArrayList<>();

        node.forEach(element ->
                result.add(element.asInt())
        );

        return result;
    }

    private Mono<JsonNode> handleResponse(JsonNode response) {

        JsonNode error = response.get("error");

        if (error != null && !error.isNull()) {
            return Mono.error(
                    new AnkiConnectException(error.asText())
            );
        }

        return Mono.just(response.get("result"));
    }

    private Long extractCardId(JsonNode response) {
        JsonNode cardIdNode = response
                .path("cardId");

        if (cardIdNode.isMissingNode() || cardIdNode.isNull()) {
            throw new IllegalStateException(
                    "guiCurrentCard did not return cardId"
            );
        }

        return cardIdNode.asLong();
    }

    private String extractVideoFilename(JsonNode response) {

        if (!response.isArray() || response.isEmpty()) {
            throw new IllegalStateException(
                    "cardsInfo did not return card information"
            );
        }

        JsonNode card = response.get(0);

        JsonNode fields = card.path("fields");

        if (!fields.isObject()) {
            throw new IllegalStateException(
                    "Card does not contain fields"
            );
        }

        Pattern pattern = Pattern.compile(
                "\\[sound:([^\\]]+)]"
        );

        Iterator<JsonNode> fieldValues = fields.elements();

        while (fieldValues.hasNext()) {

            JsonNode field = fieldValues.next();

            String value = field.path("value").asText("");

            Matcher matcher = pattern.matcher(value);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        throw new IllegalStateException(
                "No video/audio media found in current card"
        );
    }

    private byte[] decodeBase64(JsonNode response) {

        if (response.isMissingNode() || response.isNull()) {
            throw new IllegalStateException(
                    "retrieveMediaFile returned no response"
            );
        }

        if (!response.isTextual()) {
            throw new IllegalStateException(
                    "retrieveMediaFile response is not Base64 string"
            );
        }

        try {
            return Base64.getDecoder().decode(response.asText());

        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Invalid Base64 returned by AnkiConnect",
                    e
            );
        }
    }

    @Override
    public Mono<InitStrikeStatDate> initialStrikeStat(String deck) {
        return invoke("cardReviews", Map.of("deck", deck, "startID", 0L))
                .flatMapMany(Flux::fromIterable)          // разворачиваем массив ревью в поток
                .map(review -> review.get(0).asLong())     // берём reviewTime (первый элемент каждого под-массива)
                .map(this::toLocalDate)                    // unix millis -> календарная дата
                .distinct()                                // убираем дубликаты дат (несколько карточек в один день)
                .collectSortedList(Comparator.reverseOrder()) // сортируем от новых дат к старым
                .map(this::buildStrikeStat);
    }

    @Override
    public Mono<Long> lastReviewTime(String deck) {
        return invoke("cardReviews", Map.of("deck", deck, "startID", 0L))
                .flatMapMany(Flux::fromIterable)
                .map(review -> review.get(0).asLong())   // берём reviewTime каждого review
                .reduce(Math::max);
    }

    private LocalDate toLocalDate(long epochMilli) {
        return Instant.ofEpochMilli(epochMilli)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private int calculateStreak(List<LocalDate> datesDesc) {
        if (datesDesc.isEmpty()) {
            return 0;
        }

        LocalDate expected = LocalDate.now();
        long lastReviewed = expected.atStartOfDay(ZoneId.systemDefault())
                .plusDays(1L)
                .toInstant().toEpochMilli();

        // если сегодня ещё не проходили карточки — начинаем отсчёт со вчерашнего дня,
        // иначе стрик сразу обнулится, хотя пользователь ещё может успеть позаниматься сегодня
        if (!datesDesc.get(0).equals(expected)) {
            expected = expected.minusDays(1);
            lastReviewed = expected
                    .atStartOfDay(ZoneId.systemDefault())
                    .plusDays(1L)
                    .toInstant()
                    .toEpochMilli();
        }

        int streak = 0;
        for (LocalDate date : datesDesc) {
            if (date.equals(expected)) {
                streak++;
                expected = expected.minusDays(1);
            } else if (date.isBefore(expected)) {
                // нашли пропуск — дальше список можно отбрасывать
                break;
            }
        }

        if(streak == 0){

        }

        return streak;
    }

    private InitStrikeStatDate buildStrikeStat(List<LocalDate> datesDesc) {
        if (datesDesc.isEmpty()) {
            return InitStrikeStatDate.builder()
                    .strikeCount(0)
                    .lastReviewDate(0L)
                    .build();
        }

        LocalDate mostRecent = datesDesc.get(0);
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // последняя дата прохождения кладётся только если это сегодня или вчера,
        // иначе стрик уже прерван и хранить нечего — ставим 0
        long lastReviewDate = (mostRecent.equals(today) || mostRecent.equals(yesterday))
                ? toEpochMilli(mostRecent)
                : 0L;

        int strikeCount = calculateStreak(datesDesc);

        return InitStrikeStatDate.builder()
                .strikeCount(strikeCount)
                .lastReviewDate(lastReviewDate)
                .build();
    }

    private long toEpochMilli(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

}