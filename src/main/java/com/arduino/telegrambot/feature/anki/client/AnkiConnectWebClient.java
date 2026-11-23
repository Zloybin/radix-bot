package com.arduino.telegrambot.feature.anki.client;

import com.arduino.telegrambot.feature.anki.exception.AnkiConnectException;
import com.arduino.telegrambot.feature.anki.model.add.Note;
import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.*;

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

    // Version
    @Override
    public Mono<Integer> version() {
        return invoke("version", Map.of())
                .map(JsonNode::asInt);
    }

    // Decks
    @Override
    public Mono<JsonNode> getDeckNames() {
        return invoke("deckNames", Map.of());
    }

    //Completion
    @Override
    public Mono<JsonNode> guiDeckReview(String deckName) {
        return invoke(
                "guiDeckReview",
                Map.of("name", deckName)
        );
    }
    @Override
    public Mono<JsonNode> getCurrentCard() {
        return invoke("guiCurrentCard", Map.of());
    }
    @Override
    public Mono<JsonNode> showAnswer() {
        return invoke("guiShowAnswer", Map.of());
    }
    @Override
    public Mono<JsonNode> answerCard(int ease) {
        return invoke(
                "guiAnswerCard",
                Map.of("ease", ease)
                );
    }

    //Statistics
    @Override
    public Mono<JsonNode> getNumCardsReviewedToday() {
        return invoke("getNumCardsReviewedToday", Map.of());
    }
    @Override
    public Mono<JsonNode> getDecksStatistics(List<String> deckNames) {
        return invoke("getDeckStats", Map.of("decks", deckNames));
    }
    @Override
    public Mono<JsonNode> cardReviews(String deck, long startId) {
        return invoke("cardReviews", Map.of("deck", deck, "startID", startId));
    }
    @Override
    public Mono<JsonNode> findCards(String query) {
        return invoke("findCards", Map.of("query", query));
    }
    @Override
    public Mono<JsonNode> findNotes(String query) {
        return invoke("findNotes", Map.of("query", query));
    }

    //Features
    @Override
    public Mono<JsonNode> setSpecificValueOfCard(long cardId, int flag) {
        return invoke(
                "setSpecificValueOfCard",
                Map.of("card", cardId,
                        "keys", List.of("flags"),
                        "newValues", List.of(flag),
                        "warning_check", true)
        );
    }

    // Management
    @Override
    public Mono<JsonNode> addNote(Note note) {
        return invoke("addNote", Map.of("note", note));
    }
    @Override
    public Mono<JsonNode> addNotes(List<Note> notes) {
        return invoke("addNotes", Map.of("notes", notes));
    }
    @Override
    public Mono<JsonNode> suspendCards(List<Long> cards) {
        return invoke("suspend", Map.of("cards", cards));
    }
    @Override
    public Mono<JsonNode> cardsInfo(List<Long> cardIds) {
        return invoke("cardsInfo", Map.of("cards", cardIds));
    }
    @Override
    public Mono<JsonNode> reloadCollection() {
        return invoke("reloadCollection", Map.of());
    }

    //Private
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

}