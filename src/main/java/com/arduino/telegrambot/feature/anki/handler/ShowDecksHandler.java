package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.entity.DeckStrikeInfo;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ShowDecksHandler implements UpdateHandler {

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiService ankiService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "showDecks".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.WAIT_DECK_NAME);

        var decks = ankiService.getDecks().block();

        var strikes = user.getStrikes();

        var refreshedStrikes = strikes.size() == 0
                ? ankiService.initialStrikeStats(decks)
                : ankiService.refreshStrikeStats(strikes);

        user.setStrikes(refreshedStrikes);
        userService.save(user);

        var strikesTemplateData = new HashMap<String, Integer>();

        for (DeckStrikeInfo refreshedStrike : refreshedStrikes) {
           strikesTemplateData.put(refreshedStrike.getDeckName(), refreshedStrike.getStrikeCount());
        }

        var text = templateProcessor.processDecksMenuTemplate(strikesTemplateData);

        var stats = ankiService.getDecksStats(decks).block();
        var keyboard = keyboardBuilder.buildDecksMenu(decks, stats);

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }
}
