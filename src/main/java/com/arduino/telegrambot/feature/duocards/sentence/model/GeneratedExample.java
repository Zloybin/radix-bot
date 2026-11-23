package com.arduino.telegrambot.feature.duocards.sentence.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneratedExample {
    String generatedSentence;
    String generatedTranslation;
    String generatedSourceExample;
    String generatedTense;
    String generatedSubjunctive;
}
