package com.arduino.telegrambot.keyboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuType {
    MAIN("Главное меню"),
    ENGINE_CONTROL("Управление двигателем");

    private final String displayName;
}