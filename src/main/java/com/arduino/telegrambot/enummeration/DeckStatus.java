package com.arduino.telegrambot.enummeration;

public enum DeckStatus {
    NOT_STARTED ("💤"),
    IN_PROGRESS("⏳"),
    COMPLETED("✅");

    private String title;

    DeckStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
