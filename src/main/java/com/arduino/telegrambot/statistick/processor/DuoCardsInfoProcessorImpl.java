package com.arduino.telegrambot.statistick.processor;

public class DuoCardsInfoProcessorImpl implements DuoCardsInfoProcessor{
    @Override
    public boolean isDailyQuotaDuoCardsTaskCompleted(int completedDuoCardsTaskCount) {
        return completedDuoCardsTaskCount >= 5;
    }
}
