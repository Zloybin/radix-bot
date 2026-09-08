package com.arduino.telegrambot.piper;

import com.arduino.telegrambot.piper.model.PiperRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PiperTtsService {

    private final WebClient webClient;

    public PiperTtsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://piper:5000")
                .build();
    }

    public Mono<byte[]> synthesize(String text) {

        PiperRequest request = new PiperRequest(text);

        return webClient
                .post()
                .uri("/synthesize")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(byte[].class);
    }
}
