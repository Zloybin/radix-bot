package com.arduino.telegrambot.anki.test;

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

    public AnkiTestController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/test")
    public Mono<String> testAnki() {

        return webClient.post()
                .uri("http://host.docker.internal:8765")
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

//    @GetMapping("/handler/test")
//    public Mono<Integer> testAnki() {
//        return ankiClient.version();
//    }
}
