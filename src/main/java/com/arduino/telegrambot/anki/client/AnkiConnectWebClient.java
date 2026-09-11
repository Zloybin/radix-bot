package com.arduino.telegrambot.anki.client;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.anki.model.AnkiDeckStats;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AnkiConnectWebClient implements AnkiConnectClient {

    private static final int API_VERSION = 6;


    private final String ankiBaseUrl;

    private final WebClient webClient;


    public AnkiConnectWebClient(WebClient.Builder webClientBuilder, @Value("${anki.baseUrl}") String ankiBaseUrl) {
        this.ankiBaseUrl = ankiBaseUrl;
        this.webClient = webClientBuilder
                .baseUrl(ankiBaseUrl)
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)
                )
                .build();
    }

    @Override
    public Mono<Integer> version() {
        return invoke("version", Map.of())
                .map(JsonNode::asInt);
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
    public Mono<Boolean> startDeckReview(String deckName) {
        return invoke(
                "guiDeckReview",
                Map.of("name", deckName)
        ).map(JsonNode::asBoolean);
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
    public Mono<Boolean> startCardTimer() {
        return invoke("guiStartCardTimer", Map.of())
                .map(JsonNode::asBoolean);
    }

    @Override
    public Mono<Boolean> deleteCard(long cardId) {
        return invoke("suspend", Map.of("cards", List.of(cardId)))
                .map(JsonNode::asBoolean);
    }

    @Override
    public Mono<Boolean> showQuestion() {
        return invoke("guiShowQuestion", Map.of())
                .map(JsonNode::asBoolean);
    }

    @Override
    public Mono<Boolean> showAnswer() {
        return invoke("guiShowAnswer", Map.of())
                .map(JsonNode::asBoolean);
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

    private List<Integer> readIntegerList(JsonNode node) {

        List<Integer> result = new ArrayList<>();

        node.forEach(element ->
                result.add(element.asInt())
        );

        return result;
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

    private Mono<JsonNode> handleResponse(JsonNode response) {

        JsonNode error = response.get("error");

        if (error != null && !error.isNull()) {
            return Mono.error(
                    new AnkiConnectException(error.asText())
            );
        }

        return Mono.just(response.get("result"));
    }


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

    private String findVideoFilename(String html) {

        Pattern[] patterns = {
                Pattern.compile(
                        "<source[^>]+src=[\"']([^\"']+)[\"']",
                        Pattern.CASE_INSENSITIVE
                ),

                Pattern.compile(
                        "<video[^>]+src=[\"']([^\"']+)[\"']",
                        Pattern.CASE_INSENSITIVE
                )
        };

        for (Pattern pattern : patterns) {

            Matcher matcher = pattern.matcher(html);

            if (matcher.find()) {
                String src = matcher.group(1);

                return extractFilename(src);
            }
        }

        return null;
    }

    private String extractFilename(String src) {

        // Например:
        // "physics.mp4"
        // "/physics.mp4"
        // "https://example.com/physics.mp4"
        // "physics.mp4?foo=bar"

        String filename = src;

        int queryIndex = filename.indexOf('?');

        if (queryIndex >= 0) {
            filename = filename.substring(0, queryIndex);
        }

        int fragmentIndex = filename.indexOf('#');

        if (fragmentIndex >= 0) {
            filename = filename.substring(0, fragmentIndex);
        }

        int slashIndex = filename.lastIndexOf('/');

        if (slashIndex >= 0) {
            filename = filename.substring(slashIndex + 1);
        }

        return filename;
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
}