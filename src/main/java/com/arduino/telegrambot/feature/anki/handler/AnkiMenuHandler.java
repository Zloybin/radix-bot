package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.DeckProgress;
import com.arduino.telegrambot.entity.DeckStrikeInfo;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;

@Component
public class AnkiMenuHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiServiceImpl ankiService;

    @Autowired
    private UserService userService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "ankiMenu".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
        var strikes = user.getStrikes();
        var deckProgress = user.getDeckProgress();

        var decks = ankiService.getFilteredDecks().block();
        var updatedDeckProgresses = ankiService.updateUserDeckStatus(decks, deckProgress);

        var progressTemplateData = createProgressTemplateData(updatedDeckProgresses);

        var refreshedStrikes = strikes.isEmpty()
                ? ankiService.initialStrikeStats(decks)
                : ankiService.refreshStrikeStats(strikes);

        user.setStrikes(refreshedStrikes);
        userService.save(user);

        var strikesTemplateData = new HashMap<String, Integer>();

        for (DeckStrikeInfo refreshedStrike : refreshedStrikes) {
            strikesTemplateData.put(refreshedStrike.getDeckName(), refreshedStrike.getStrikeCount());
        }

        var keyboard = keyboardBuilder.buildAnkiMenu(decks);

        var numCardsReviewedToday = ankiService.getNumCardsReviewedToday().block();
        var text = templateProcessor.processAnkiUserProfileTemplate(numCardsReviewedToday, strikesTemplateData, progressTemplateData);
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }

    private HashMap<String, String> createProgressTemplateData(List<DeckProgress> updatedDeckProgresses) {
        var progressTemplateData = new HashMap<String, String>();

        for (DeckProgress updatedDeckProgress : updatedDeckProgresses) {
            progressTemplateData.put(updatedDeckProgress.getDeckName(), updatedDeckProgress.getDeckStatus().getTitle());
        }
        return progressTemplateData;
    }
}
