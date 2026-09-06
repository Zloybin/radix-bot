package com.arduino.telegrambot.config;

import com.arduino.telegrambot.ai.GroqLLMService;
import com.arduino.telegrambot.ai.LLMService;
import com.google.genai.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class AppConfig {

    @Bean
    public TemplateEngine templateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }

    @Bean
    public Client client() {
        return Client.builder()
                .build();
    }

    @Bean
    public LLMService llmService() {
        return new GroqLLMService();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
