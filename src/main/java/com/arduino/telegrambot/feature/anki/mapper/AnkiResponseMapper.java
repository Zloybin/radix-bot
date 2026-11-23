package com.arduino.telegrambot.feature.anki.mapper;


import com.arduino.telegrambot.feature.anki.model.AnkiCurrentCard;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

public interface AnkiResponseMapper {
    List<String> toDeckName(JsonNode jsonNode);
    Map<String, AnkiDeckStats> toDeckStatisticsMap(JsonNode jsonNode);
    List<Long> toCardIdList(JsonNode jsonNode);
    List<Long> mapFindNotes(JsonNode jsonNode);
    AnkiCurrentCard toAnkiCurrentCard(JsonNode jsonNode);
}
