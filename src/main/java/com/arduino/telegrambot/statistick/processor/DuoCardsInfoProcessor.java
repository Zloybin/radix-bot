package com.arduino.telegrambot.statistick.processor;

import org.springframework.stereotype.Component;

@Component
public interface DuoCardsInfoProcessor {
    boolean isDailyQuotaDuoCardsTaskCompleted (int completedDuoCardsTaskCount);
}
