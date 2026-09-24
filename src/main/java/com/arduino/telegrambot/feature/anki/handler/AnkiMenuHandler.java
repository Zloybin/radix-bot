package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.DeckProgress;
import com.arduino.telegrambot.entity.DeckStrikeInfo;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.DeckStatus;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private AnkiService ankiService;

    @Autowired
    private UserService userService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "ankiMenu".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());


        var decks = ankiService.getDecks().block();
        List<String> filteredDeckList = decks.stream().filter(deck -> !AnkiUtility.EXCLUDED_DECKS.contains(deck)).toList();
        var strikes = user.getStrikes();

        var deckProgress = user.getDeckProgress();

        if(deckProgress.size() == 0){

            List<DeckProgress>deckPro = new ArrayList<>();
            for (String deck : filteredDeckList) {
                DeckProgress build = DeckProgress.builder()
                        .deckName(deck)
                        .localDate(0L)
                        .deckStatus(DeckStatus.NOT_STARTED)
                        .user(userService.findById(userRequest.getChatId()))
                        .build();
                deckPro.add(build);
            }
            user.setDeckProgress(deckPro);
            user.setState(UserState.WAIT_DECK_NAME);
            userService.save(user);
        }

        User byId = userService.findById(userRequest.getChatId());


        var updatedDeckProgresses = ankiService.updateUserDeckStatus(decks, byId.getDeckProgress());

        var progressTemplateData = new HashMap<String, String>();

        for (DeckProgress updatedDeckProgress : updatedDeckProgresses) {
            progressTemplateData.put(updatedDeckProgress.getDeckName(), updatedDeckProgress.getDeckStatus().getTitle());
        }

        var refreshedStrikes = strikes.size() == 0
                ? ankiService.initialStrikeStats(decks)
                : ankiService.refreshStrikeStats(strikes);

        user.setStrikes(refreshedStrikes);
        userService.save(user);

        var strikesTemplateData = new HashMap<String, Integer>();

        for (DeckStrikeInfo refreshedStrike : refreshedStrikes) {
            strikesTemplateData.put(refreshedStrike.getDeckName(), refreshedStrike.getStrikeCount());
        }



        var stats = ankiService.getDecksStats(decks).block();
        var keyboard = keyboardBuilder.buildDecksMenu(decks, stats);

        var numCardsReviewedToday = ankiService.getNumCardsReviewedToday().block();
        var text = templateProcessor.processAnkiUserProfileTemplate(numCardsReviewedToday, strikesTemplateData, progressTemplateData);
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }
}
