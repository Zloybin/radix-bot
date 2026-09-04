package com.arduino.telegrambot.youglish;

import com.arduino.telegrambot.youglish.model.YouglishResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class YouglishService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://youglish.com/api/v1/";

    /**
     * Поиск видео по фразе
     */
    public List<YouglishResponse.VideoResult> searchVideos(String phrase, int limit) {
        String url = UriComponentsBuilder.fromHttpUrl(API_URL + "search")
                .queryParam("query", phrase)
                // Количество видео
                .queryParam("lang", "English")        // Язык
                .build()
                .toUriString();

        try {
            YouglishResponse response = restTemplate.getForObject(url, YouglishResponse.class);
            return response != null ? response.getResults() : List.of();
        } catch (Exception e) {
            log.error("Ошибка при запросе к Youglish: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Формирует embed-ссылку для Telegram
     */
    public String buildVideoUrl(String videoId, int startSeconds) {
        return String.format("https://www.youtube.com/watch?v=%s&t=%ds", videoId, startSeconds);
    }

    /**
     * Извлекает videoId из URL YouTube
     */
    public String extractVideoId(String youtubeUrl) {
        // Простая реализация для примера
        String[] parts = youtubeUrl.split("v=");
        if (parts.length > 1) {
            return parts[1].split("&")[0];
        }
        return null;
    }
}
