package com.arduino.telegrambot.anki.client;

import com.arduino.telegrambot.anki.AnkiConnectException;
import com.arduino.telegrambot.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.anki.model.AnkiDeckStats;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AnkiConnectWebClient implements AnkiConnectClient {

    private static final int API_VERSION = 6;

    private final WebClient webClient;

    public AnkiConnectWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://host.docker.internal:8765")
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
            Map<String, AnkiDeckStats> result = new java.util.HashMap<>();

            json.fields().forEachRemaining(entry -> {

                AnkiDeckStats stats =
                        new AnkiDeckStats(
                                entry.getValue().get("deck_id").asLong(),
                                entry.getValue().get("name").asText(),
                                entry.getValue().get("new_count").asInt(),
                                entry.getValue().get("learn_count").asInt(),
                                entry.getValue().get("review_count").asInt(),
                                entry.getValue().get("total_in_deck").asInt()
                        );

                result.put(entry.getKey(), stats);
            });

            return result;
        });
    }

    @Override
    public Mono<AnkiCurrentCard> getCurrentCard() {
        return invoke("guiCurrentCard", Map.of())
                .map(json -> new AnkiCurrentCard(
                        json.get("cardId").asLong(),
                        json.get("question").asText(),
                        json.get("answer").asText(),
                        readIntegerList(json.get("buttons"))
                ));
    }

    @Override
    public Mono<Boolean> startCardTimer() {
        return invoke("guiStartCardTimer", Map.of())
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
                {
                    var currentCard = new AnkiCurrentCard(Long.parseLong(response.get("chatId").asText()), response.get("question").asText(), response.get("answer").asText(), new ArrayList<Integer>());
                    System.out.println("ANKI RESPONSE: " + currentCard);})

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
}