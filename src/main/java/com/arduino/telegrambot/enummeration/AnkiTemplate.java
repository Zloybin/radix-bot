package com.arduino.telegrambot.enummeration;

import lombok.Getter;

@Getter
public enum AnkiTemplate {
    TEMPLATE_1("Карточка 1"),
    TEMPLATE_2("Карточка 2");

    private String titile;

    AnkiTemplate(String titile) {
        this.titile = titile;
    }

}
