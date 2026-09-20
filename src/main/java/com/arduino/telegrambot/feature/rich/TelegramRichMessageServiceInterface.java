package com.arduino.telegrambot.feature.rich;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TelegramRichMessageServiceInterface {
    Mono<Long> sendRichMessage(
            long chatId,
            String html,
            ReplyKeyboard keyboard);

    Mono<String> editRichMessage(
            long chatId,
            long messageId,
            String html,
            InlineKeyboardMarkup keyboard
    );

    Mono<String> editRichMessageReplyMarkup(
            long chatId,
            long messageId,
            InlineKeyboardMarkup keyboard
    );

    Mono<String> editRichMessageWithAudio(
            long chatId,
            long messageId,
            String html,
            byte[] audioBytes,
            InlineKeyboardMarkup keyboard
    );

    Mono<String> editRichMessageWithAudioAndVideo(
            long chatId,
            long messageId,
            String html,
            byte[] audioBytes,
            byte[] videoBytes,
            InlineKeyboardMarkup keyboard
    );

    Mono<String> editRichMessageForcedReplyMarkup(
            long chatId,
            int messageId,
            String text,
            ReplyKeyboard replyKeyboardMarkup
    );
}
