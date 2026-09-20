package com.arduino.telegrambot.feature.anki.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/handler")
public class AnkiTestController {

    private final WebClient webClient;
    private final String ankiBaseUrl;

    public AnkiTestController(WebClient.Builder webClientBuilder, @Value("${anki.baseUrl}") String ankiBaseUrl) {
        this.webClient = webClientBuilder.build();
        this.ankiBaseUrl = ankiBaseUrl;
    }

    @GetMapping("/test")
    public Mono<String> testAnki() {

        return webClient.post()
                .uri(ankiBaseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                            "action": "version",
                            "version": 6
                        }
                        """)
                .retrieve()
                .bodyToMono(String.class);
    }
}
