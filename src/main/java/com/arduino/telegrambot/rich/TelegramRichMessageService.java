package com.arduino.telegrambot.rich;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TelegramRichMessageService {

    private final WebClient webClient;
    private final String botToken;
    private final ObjectMapper objectMapper;

    public TelegramRichMessageService(
            WebClient.Builder webClientBuilder,
            @Value("${telegram.bot.token}") String botToken,
            ObjectMapper objectMapper
    ) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.telegram.org")
                .build();

        this.botToken = botToken;
        this.objectMapper = objectMapper;
    }

    public Mono<String> sendRichMessage(
            long chatId,
            String html,
            InlineKeyboardMarkup keyboard
    ) {
        Map<String, Object> richMessage = Map.of(
                "html", html
        );

        Map<String, Object> request = new HashMap<>();

        request.put("chat_id", chatId);
        request.put("rich_message", richMessage);
        request.put("reply_markup", keyboard);

        return webClient.post()
                .uri("/bot{token}/sendRichMessage", botToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> editRichMessage(
            long chatId,
            long messageId,
            String html,
            InlineKeyboardMarkup keyboard
    ) {
        Map<String, Object> richMessage = Map.of(
                "html", html
        );

        Map<String, Object> request = new HashMap<>();

        request.put("chat_id", chatId);
        request.put("message_id", messageId);
        request.put("rich_message", richMessage);
        request.put("reply_markup", keyboard);

        return webClient.post()
                .uri("/bot{token}/editMessageText", botToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> editRichMessageWithAudio(
            long chatId,
            long messageId,
            String html,
            byte[] audioBytes,
            InlineKeyboardMarkup keyboard
    ) {

        String mediaId = "audio_1";
        String fileFieldName = "audio";

        Map<String, Object> inputMediaAudio = new HashMap<>();
        inputMediaAudio.put("type", "audio");
        inputMediaAudio.put("media", "attach://" + fileFieldName);

        Map<String, Object> richMedia = new HashMap<>();
        richMedia.put("id", mediaId);
        richMedia.put("media", inputMediaAudio);

        Map<String, Object> richMessage = new HashMap<>();
        richMessage.put("html", html);
        richMessage.put("media", List.of(richMedia));

        MultipartBodyBuilder multipart = new MultipartBodyBuilder();

        multipart.part("chat_id", chatId);
        multipart.part("message_id", messageId);

        try {
            multipart.part(
                    "rich_message",
                    objectMapper.writeValueAsString(richMessage)
            ).contentType(MediaType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (keyboard != null) {
            try {
                multipart.part(
                        "reply_markup",
                        objectMapper.writeValueAsString(keyboard)
                ).contentType(MediaType.APPLICATION_JSON);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        multipart.part(
                fileFieldName,
                new ByteArrayResource(audioBytes) {
                    @Override
                    public String getFilename() {
                        return "Прослушать \uD83D\uDD0A";
                    }
                }
        ).contentType(MediaType.parseMediaType("audio/mpeg"));

        return webClient.post()
                .uri("/bot{token}/editMessageText", botToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipart.build()))
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    System.err.println(
                                            "Telegram error: " + body
                                    );

                                    return Mono.error(
                                            new RuntimeException(body)
                                    );
                                })
                )
                .bodyToMono(String.class);
    }

    public Mono<String> editRichMessageWithAudioAndVideo(
            long chatId,
            long messageId,
            String html,
            byte[] audioBytes,
            byte[] videoBytes,
            InlineKeyboardMarkup keyboard
    ) {

        String audioMediaId = "audio_1";
        String audioFileFieldName = "audio";

        String videoMediaId = "video_1";
        String videoFileFieldName = "video";


        // ============================================================
        // AUDIO
        // ============================================================

        Map<String, Object> inputMediaAudio = new HashMap<>();
        inputMediaAudio.put("type", "audio");
        inputMediaAudio.put(
                "media",
                "attach://" + audioFileFieldName
        );

        Map<String, Object> richMediaAudio = new HashMap<>();
        richMediaAudio.put("id", audioMediaId);
        richMediaAudio.put("media", inputMediaAudio);


        // ============================================================
        // VIDEO
        // ============================================================

        Map<String, Object> inputMediaVideo = new HashMap<>();
        inputMediaVideo.put("type", "video");
        inputMediaVideo.put(
                "media",
                "attach://" + videoFileFieldName
        );

        Map<String, Object> richMediaVideo = new HashMap<>();
        richMediaVideo.put("id", videoMediaId);
        richMediaVideo.put("media", inputMediaVideo);


        // ============================================================
        // RICH MESSAGE
        // ============================================================

        Map<String, Object> richMessage = new HashMap<>();

        richMessage.put("html", html);

        richMessage.put(
                "media",
                List.of(
                        richMediaAudio,
                        richMediaVideo
                )
        );


        // ============================================================
        // MULTIPART
        // ============================================================

        MultipartBodyBuilder multipart = new MultipartBodyBuilder();

        multipart.part("chat_id", chatId);
        multipart.part("message_id", messageId);


        // ============================================================
        // rich_message JSON
        // ============================================================

        try {

            multipart.part(
                    "rich_message",
                    objectMapper.writeValueAsString(richMessage)
            ).contentType(MediaType.APPLICATION_JSON);

        } catch (JsonProcessingException e) {

            return Mono.error(e);
        }


        // ============================================================
        // KEYBOARD
        // ============================================================

        if (keyboard != null) {

            try {

                multipart.part(
                        "reply_markup",
                        objectMapper.writeValueAsString(keyboard)
                ).contentType(MediaType.APPLICATION_JSON);

            } catch (JsonProcessingException e) {

                return Mono.error(e);
            }
        }


        // ============================================================
        // AUDIO FILE
        // ============================================================

        multipart.part(
                audioFileFieldName,
                new ByteArrayResource(audioBytes) {

                    @Override
                    public String getFilename() {
                        return "audio.mp3";
                    }
                }
        ).contentType(
                MediaType.parseMediaType("audio/mpeg")
        );


        // ============================================================
        // VIDEO FILE
        // ============================================================

        multipart.part(
                videoFileFieldName,
                new ByteArrayResource(videoBytes) {

                    @Override
                    public String getFilename() {
                        return "video.mp4";
                    }
                }
        ).contentType(
                MediaType.parseMediaType("video/mp4")
        );


        // ============================================================
        // REQUEST
        // ============================================================

        return webClient.post()
                .uri(
                        "/bot{token}/editMessageText",
                        botToken
                )
                .contentType(
                        MediaType.MULTIPART_FORM_DATA
                )
                .body(
                        BodyInserters.fromMultipartData(
                                multipart.build()
                        )
                )
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> editRichMessageReplyMarkup(
            long chatId,
            long messageId,
            InlineKeyboardMarkup keyboard
    ) {

        Map<String, Object> request = new HashMap<>();

        request.put("chat_id", chatId);
        request.put("message_id", messageId);
        request.put("reply_markup", keyboard);

        String json = null;
        try {
            json = objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println(json);


        return webClient.post()
                .uri("/bot{token}/editMessageReplyMarkup", botToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class);
    }

}
