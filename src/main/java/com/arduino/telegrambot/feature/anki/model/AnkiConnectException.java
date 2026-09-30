package com.arduino.telegrambot.feature.anki.model;

public class AnkiConnectException extends RuntimeException {

    public AnkiConnectException(String message) {
        super(message);
    }

    public AnkiConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    /** true, если AnkiConnect отклонил заметку как дубликат. */
    public boolean isDuplicate() {
        return getMessage() != null && getMessage().contains("duplicate");
    }
}
