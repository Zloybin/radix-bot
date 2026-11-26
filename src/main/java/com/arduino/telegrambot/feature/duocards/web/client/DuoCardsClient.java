package com.arduino.telegrambot.feature.duocards.web.client;

import com.arduino.telegrambot.feature.duocards.web.model.DuoCard;
import com.arduino.telegrambot.feature.duocards.web.model.DuoCardsGraphQLResponse;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DuoCardsClient {

    @Qualifier("duoCardsWebClient")
    private final WebClient webClient;

    @Value("${duocards.token}")
    private String token;

//    @Value("${duocards.deck-id}")
    private String deckId;

    public DuoCard createCard(
            String sentence,
            String translation,
            String example
    ) {

        String query = """
                mutation cardCreateMutation(
                  $deckId: ID!
                  $front: String!
                  $back: String!
                  $langBack: String
                  $hint: String
                  $sCardId: ID
                  $sBackId: ID
                  $sourceId: ID
                  $deleted: Boolean
                  $returnDeck: Boolean
                ) {
                  cardCreate(
                    deckId: $deckId
                    front: $front
                    back: $back
                    langBack: $langBack
                    hint: $hint
                    sCardId: $sCardId
                    sBackId: $sBackId
                    sourceId: $sourceId
                    deleted: $deleted
                    returnDeck: $returnDeck
                  ) {
                    card {
                      id
                      deckId
                      front
                      back
                      hint
                      knownCount
                      failCount
                      flipped
                      knownAt
                      knownUntil
                      loopedAt
                    }
                    deck {
                      id
                    }
                  }
                }
                """;

        Map<String, Object> variables = new HashMap<>();

        variables.put("deckId", "RGVjazo4YzJhYmIxOS0yZjcwLTRkYTktYjc1ZC05MDdkNjE3ZTZhMTk=");
        variables.put("front", sentence);
        variables.put("back", translation);
        variables.put("langBack", "ru");
        variables.put("hint", example);
        variables.put("sCardId", null);
        variables.put("sBackId", null);
        variables.put("sourceId", null);
        variables.put("deleted", null);
        variables.put("svg", null);
        variables.put("returnDeck", false);

        Map<String, Object> body = Map.of(
                "query", query,
                "variables", variables
        );

        System.out.println("DuoCards request:");
        System.out.println(body);

        DuoCardsGraphQLResponse response = WebClient.builder()
                .baseUrl("https://api.duocards.com")
                .build()
//                webClient

                .post()
                .uri("/graphql?cardCreateMutation")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + token
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        responsed -> responsed.bodyToMono(String.class)
                                .map(bodyd -> new RuntimeException(
                                        "DuoCards API error: "
                                                + responsed.statusCode()
                                                + "\n"
                                                + bodyd
                                ))
                )

                .bodyToMono(DuoCardsGraphQLResponse.class)
                .block();

        if (response == null
                || response.data() == null
                || response.data().cardCreate() == null
                || response.data().cardCreate().card() == null) {

            throw new IllegalStateException(
                    "DuoCards did not return created card"
            );
        }

        return response.data()
                .cardCreate()
                .card();
    }

    public Mono<Boolean> isStreakReceivedToday() {

        return getViewerProgress()
                .map(response -> {

                    JsonNode viewer = response.path("data").path("viewer");

                    String timezone = viewer.path("timezone").asText("Europe/Berlin");

                    LocalDate today = LocalDate.now(ZoneId.of(timezone));

                    JsonNode days = viewer.path("xps").path("days");

                    for (JsonNode day : days) {

                        LocalDate date = LocalDate.parse(day.path("when").asText());

                        if (date.equals(today)) {
                            return day.path("streaked").asBoolean(false);
                        }
                    }

                    return false;
                });
    }

    public Mono<JsonNode> getViewerProgress() {

        String query = """
            query viewerQuery {
              viewer {
                timezone
                xps {
                  days {
                    when
                    streaked
                  }
                }
              }
            }
            """;

        Map<String, Object> body = Map.of(
                "query", query,
                "variables", Map.of()
        );

        return WebClient.builder()
                .baseUrl("https://api.duocards.com")
                .build()
                .post()
                .uri("/graphql?viewerQuery")
                .header(HttpHeaders.ORIGIN, "https://app.duocards.com")
                .header(HttpHeaders.REFERER, "https://app.duocards.com/")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + token
                )
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}