package com.arduino.telegrambot.statistick.processor;

public class PhysTaskInfoProcessorImpl implements PhysTaskInfoProcessor{
    @Override
    public boolean isDailyQuotaPhysTaskCompleted(int completedPhysTaskCount) {
        return completedPhysTaskCount >= 5;
    }
}
