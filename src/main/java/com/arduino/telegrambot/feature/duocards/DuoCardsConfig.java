package com.arduino.telegrambot.feature.duocards;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DuoCardsConfig {

//    @Bean
    public WebClient duoCardsWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.duocards.com")
                .build();
    }
}
