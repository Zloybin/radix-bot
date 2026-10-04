package com.arduino.telegrambot.feature.duocards.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.feature.ai.groq.GroqLLMService;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.duocards.model.GeneratedExample;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;
import java.util.Random;

@Component
public class ExerciseSentenceHandler implements UpdateHandler {

    @Autowired
    private AnkiService ankiService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private GroqLLMService groqLLMService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "sentenceExercise".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var cards = ankiService.findCards("Deutsch");
        var random = new Random();
        var cardIdIndex = random.nextInt(0, cards.size());

        Long cardId = cards.get(cardIdIndex);

        var sentenceExample = ankiService.cardsInfo(cardId);
        var generatedExample = groqLLMService.generateFromCard(sentenceExample.getFront());
        var user = userService.findById(userRequest.getChatId());
        user.setGeneratedExample(generatedExample);
        userService.save(user);

        var text = templateProcessor.processExerciseSentanceTemplate(generatedExample.getGeneratedTranslation());
        var keyboard = keyboardBuilder.buildExerciseSentenceKeyboard();
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }
}
