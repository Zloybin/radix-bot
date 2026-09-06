package com.arduino.telegrambot.config;

import com.google.common.net.HttpHeaders;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(GroqProperties.class)
public class GroqConfig {

    @Bean
    public WebClient groqWebClient(
            WebClient.Builder builder,
            GroqProperties properties
    ) {

        System.out.println(
                "Groq API key exists: " +
                        (properties.apiKey() != null && !properties.apiKey().isBlank())
        );

        System.out.println(
                "Groq API key length: " +
                        (properties.apiKey() == null
                                ? 0
                                : properties.apiKey().length())
        );

        System.out.println(
                "Groq base URL: " + properties.baseUrl()
        );

        System.out.println(
                "Groq model: " + properties.model()
        );

        return builder
                .baseUrl(properties.baseUrl())
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + properties.apiKey()
                )
                .defaultHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }
}