package com.arduino.telegrambot.feature.duocards.client;

import com.arduino.telegrambot.feature.duocards.model.DuoCard;
import com.arduino.telegrambot.feature.duocards.model.DuoCardsGraphQLResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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
}