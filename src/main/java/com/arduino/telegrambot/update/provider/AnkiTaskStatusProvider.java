package com.arduino.telegrambot.update.provider;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.entity.AnkiDeckDailyInfo;
import com.arduino.telegrambot.feature.anki.model.raw.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.anki.entity.AnkiDeckDailyInfoId;
import com.arduino.telegrambot.update.enumeration.TaskType;
import com.arduino.telegrambot.update.enumeration.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class AnkiTaskStatusProvider
        implements TaskStatusProvider {

    private final AnkiService ankiService;

    @Override
    public TaskType getSource() {
        return TaskType.ANKI;
    }

    @Override
    public TaskStatus getStatus(User user, LocalDate date) {
        List<AnkiDeckDailyInfo> ankiDeckDailyInfoList;
        if (LocalDate.now(ZoneId.systemDefault()).isEqual(date)) {

            ankiDeckDailyInfoList = getUpdatedAnkiDeckDailyInfoList(user, date);


        } else {
            ankiDeckDailyInfoList = ankiService.findAnkiDeckDailyInfoListUserByDate(user, date);
        }


        boolean isCompleted = !ankiDeckDailyInfoList.isEmpty()
                && ankiDeckDailyInfoList.stream()
                .allMatch(ankiDeckDailyInfo ->
                        ankiDeckDailyInfo.getUncompletedCardsCount() == 0
                );
        return isCompleted ? TaskStatus.COMPLETED : TaskStatus.NOT_COMPLETED;
    }

    private @NonNull List<AnkiDeckDailyInfo> getUpdatedAnkiDeckDailyInfoList(User user, LocalDate date) {
        List<String> deckNameList = ankiService.getFilteredChildDeckNames().block();
        Map<String, AnkiDeckStats> ankiDecksStatisticsMap = ankiService.getAnkiDecksStatisticsMap(deckNameList).block();

        return getAnkiDeckDailyInfoList(user, date, deckNameList, ankiDecksStatisticsMap);
    }

    private List<AnkiDeckDailyInfo> getAnkiDeckDailyInfoList(User user, LocalDate date, List<String> deckNameList, Map<String, AnkiDeckStats> ankiDecksStatisticsMap) {

        List<AnkiDeckDailyInfo> ankiDeckDailyInfos = new ArrayList<>();

        for (String deckName : deckNameList) {

            var ankiDeckDailyInfoId = AnkiDeckDailyInfoId.builder()
                    .userId(user.getId())
                    .deckName(deckName)
                    .date(date)
                    .build();

            var ankiDeckStats = ankiDecksStatisticsMap.get(deckName);

            var ankiDeckDailyInfo = AnkiDeckDailyInfo.builder()
                    .ankiDeckDailyInfoId(ankiDeckDailyInfoId)
                    .newCardsLearnedCount(ankiService.getNewCardsLearnCount(deckName).block())
                    .addedNotesCount(ankiService.getAddedNotesCount(deckName).block())
                    .reviewedCardsCount(ankiService.getReviewedCardsCount(deckName).block())
                    .uncompletedCardsCount(ankiDeckStats.getReviewCount() + ankiDeckStats.getNewCount() + ankiDeckStats.getLearnCount())
                    .build();

            ankiDeckDailyInfos.add(ankiDeckDailyInfo);
        }

        return ankiDeckDailyInfos;
    }
}