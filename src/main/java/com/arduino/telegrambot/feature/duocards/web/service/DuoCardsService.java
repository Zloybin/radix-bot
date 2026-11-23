package com.arduino.telegrambot.feature.duocards.web.service;

import com.arduino.telegrambot.feature.duocards.web.client.DuoCardsClient;
import com.arduino.telegrambot.feature.duocards.web.model.DuoCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DuoCardsService {

    private final DuoCardsClient duoCardsClient;

    public DuoCard createCard(
            String sentence,
            String translation,
            String example
    ) {
        return duoCardsClient.createCard(
                sentence,
                translation,
                example
        );
    }
}