package com.arduino.telegrambot.feature.duocards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCheckResult {
    boolean isCorrect;
    boolean targetConstructionUsedCorrectly;
    boolean tenseCorrect;
    List<AnswerCheckError> errors;
    String correctedSentence;
    String feedback;
}