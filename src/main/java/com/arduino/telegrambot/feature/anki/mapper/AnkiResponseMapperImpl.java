package com.arduino.telegrambot.feature.anki.mapper;

import com.arduino.telegrambot.feature.duocards.deck.enummeration.AnkiTemplate;
import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import com.fasterxml.jackson.databind.JsonNode;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnkiResponseMapperImpl implements AnkiResponseMapper {
    @Override
    public List<String> toDeckName(JsonNode jsonNode) {
        List<String> decks = new ArrayList<>();
        jsonNode.elements().forEachRemaining(
                element -> decks.add(element.asText())
        );
        return decks;
    }

    @Override
    public Map<String, AnkiDeckStats> toDeckStatisticsMap(JsonNode jsonNode) {
        Map<String, AnkiDeckStats> result = new HashMap<>();

        jsonNode.fields().forEachRemaining(entry -> {

            AnkiDeckStats stats = AnkiDeckStats.builder()
                    .newCount(entry.getValue().get("new_count").asInt())
                    .learnCount(entry.getValue().get("learn_count").asInt())
                    .reviewCount(entry.getValue().get("review_count").asInt())
                    .totalInDeck(entry.getValue().get("total_in_deck").asInt())
                    .build();

            String deckName = entry.getValue().get("name").asText();


            result.put(deckName, stats);
        });

        return result;

    }

    @Override
    public List<Long> toCardIdList(JsonNode jsonNode) {
        List<Long> ids = new ArrayList<>();
        jsonNode.forEach(node -> ids.add(node.isNull() ? null : node.asLong()));
        return ids;
    }

    @Override
    public List<Long> mapFindNotes(JsonNode jsonNode) {
        List<Long> ids = new ArrayList<>();
        jsonNode.forEach(node -> ids.add(node.isNull() ? null : node.asLong()));
        return ids;
    }

    @Override
    public AnkiCurrentCard toAnkiCurrentCard(JsonNode jsonNode) {
        JsonNode card = jsonNode.isArray() ? jsonNode.path(0) : jsonNode;

        return AnkiCurrentCard.builder()
                        .cardId(card.path("cardId").asLong())
                .deckName(card.path("deckName").asText())
                .question(card.path("fields").path("Front").path("value").asText())
                .answer(card.path("fields").path("Back").path("value").asText())
                .example(card.path("fields").path("Beispiel").path("value").asText())
                .tags(Jsoup.parse(
                        card.path("fields").path("DisplayTags").path("value").asText("")
                ).select(".tag").eachText())
                .ankiTemplate(AnkiTemplate.TEMPLATE_1.getTitile().equals(card.path("template").asText())
                        ? AnkiTemplate.TEMPLATE_1
                        : AnkiTemplate.TEMPLATE_2)
                .flag(extractFlag(card))
                .build();
    }

    private Integer extractFlag(JsonNode cardsInfoResult) {
        if (cardsInfoResult == null || !cardsInfoResult.isArray() || cardsInfoResult.isEmpty()) {
            return 0;
        }

        return cardsInfoResult.get(0).path("flags").asInt(0);
    }
}
