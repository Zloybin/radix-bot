package com.arduino.telegrambot.piper;

import com.arduino.telegrambot.piper.model.PiperRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PiperTtsService {

    private final WebClient webClient;

    private final String baseUrl;

    public PiperTtsService(WebClient.Builder webClientBuilder, @Value("${piper.url}") String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
        this.baseUrl = baseUrl;
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
