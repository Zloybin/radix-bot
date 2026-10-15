package com.arduino.telegrambot.enummeration;

import lombok.Getter;

@Getter
public enum TelegramMediaType {
    PHOTO("photo"),
    VIDEO("video"),
    ANIMATION("animation"),
    AUDIO("audio"),
    VOICE("voice"),
    VIDEO_NOTE("video_note"),
    DOCUMENT("document"),
    STICKER("sticker"),
    LIVE_PHOTO("live_photo"),
    PAID_MEDIA("paid_media"),
    STORY("story");

    private final String field;

    TelegramMediaType(String field) {
        this.field = field;
    }
}
