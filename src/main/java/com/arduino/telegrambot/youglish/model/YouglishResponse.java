package com.arduino.telegrambot.youglish.model;

import lombok.Data;
import java.util.List;

@Data
public class YouglishResponse {
    private List<VideoResult> results;
    private int totalResults;

    @Data
    public static class VideoResult {
        private String title;
        private String url;          // Ссылка на YouTube
        private String startTime;    // Время в видео (сек)
        private String transcribedText;
        private String channelName;
    }
}
