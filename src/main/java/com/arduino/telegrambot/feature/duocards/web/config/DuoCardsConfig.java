package com.arduino.telegrambot.feature.duocards.web.config;

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
