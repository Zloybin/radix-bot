package com.arduino.telegrambot.feature.rich;

import com.arduino.telegrambot.feature.duocards.deck.entity.TelegramMedia;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import reactor.core.publisher.Mono;

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

    Mono<Long> sendRichMessageWithMedia(
            long chatId,
            String html,
            ReplyKeyboard keyboard,
            TelegramMedia media
    );

    Mono<String> editRichMessageWithMedia(
            long chatId,
            long messageId,
            String html,
            InlineKeyboardMarkup keyboard,
            TelegramMedia media
    );
}
