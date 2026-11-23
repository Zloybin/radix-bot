package com.arduino.telegrambot.feature.anki.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.feature.anki.model.AnkiMenuDeckInfo;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.feature.anki.util.AnkiUtility;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AnkiMenuHandler implements UpdateHandler {

    public static final DayOfWeek MONDAY = DayOfWeek.MONDAY;
    public static final DayOfWeek SUNDAY = DayOfWeek.SUNDAY;

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

        List<String> deckNameList = ankiService.getFilteredChildDeckNames().block();

        List<AnkiMenuDeckInfo> ankiMenuDeckInfoList = new ArrayList<>();

        deckNameList.forEach(deckName -> {

            AnkiDeckStats ankiDeckStats = ankiService.getAnkiDeckStatistics(deckName).block();
            int uncompletedCardCount = ankiDeckStats.getLearnCount() + ankiDeckStats.getReviewCount() + ankiDeckStats.getNewCount();

            AnkiMenuDeckInfo ankiMenuDeckInfo = AnkiMenuDeckInfo.builder()
                    .deckName(deckName)
                    .reviewedCardCount(ankiService.getReviewedCardsCount(deckName).block())
                    .uncompletedCardCount(uncompletedCardCount)
                    .deckStrike(ankiService.getDeckStrikeValue(deckName).block())
                    .build();

            ankiMenuDeckInfoList.add(ankiMenuDeckInfo);
        });


        var keyboard = keyboardBuilder.buildAnkiMenu(deckNameList);

        var numCardsReviewedToday = ankiService.getNumCardsReviewedToday().block();
        var text = templateProcessor.processAnkiUserProfileTemplate(ankiMenuDeckInfoList);
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }


}
