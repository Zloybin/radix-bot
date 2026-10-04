package com.arduino.telegrambot.feature.anki.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfo {

    private long cardId;

    private String front;

    @JsonProperty("fields")
    private void unpackFields(Map<String, Map<String, Object>> fields) {
        if (fields == null) {
            return;
        }
        Map<String, Object> frontField = fields.get("Front");
        if (frontField != null) {
            this.front = (String) frontField.get("value");
        }
    }
}
