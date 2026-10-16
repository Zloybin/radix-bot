package com.arduino.telegrambot.statistick.processor;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.feature.anki.model.AnkiDeckStats;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.statistick.model.DayStrikeTemplateDate;
import com.arduino.telegrambot.statistick.model.DayTaskCompletionInfo;
import com.arduino.telegrambot.statistick.model.DayDeckTemplateDate;
import com.arduino.telegrambot.statistick.model.DayUserStatisticsTemplateDate;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


public class StatisticsProcessorImpl implements StatisticsProcessor {

    public static final DayOfWeek MONDAY = DayOfWeek.MONDAY;
    public static final DayOfWeek SUNDAY = DayOfWeek.SUNDAY;

    @Autowired
    private AnkiService ankiService;

    public DayUserStatisticsTemplateDate getUserStatistics(User user) {

        var today = LocalDate.now();
        var day = today.with(SUNDAY);
        var startOfWeek = today.with(MONDAY);

        int completedPhysTaskCount = 0;
        int completedDuoCardsSentenceTaskCount = 0;
        Map<String, Boolean> deckInfoMap = new HashMap<>();
        Map<LocalDate, Boolean> weekStrikeInfoMap = new HashMap<>();
        boolean isPhysTaskCompleted = false;
        boolean isDuoCardsSentenceTaskCompleted = false;

        var dayStrikeInfoMap = user.getDayStrikeInfoMap();

        while (isWeekDay(day, startOfWeek)) {

            var dayStrikeInfo = dayStrikeInfoMap.get(day);

            if (isTodayOrDayBeforeToday(day, today)) {

                if (dayStrikeInfo == null) {
                    dayStrikeInfo = getDefaultDayStrikeInfo(user, day);
                    user.getDayStrikeInfoMap().put(day, dayStrikeInfo);
                }

                if (day.equals(today)) {

                    var todaySummaryDayStrikeInfo = dayStrikeInfo;

                    completedPhysTaskCount = todaySummaryDayStrikeInfo.getCompletedPhysTaskCount();
                    isPhysTaskCompleted = completedPhysTaskCount >= 5;

                    completedDuoCardsSentenceTaskCount = todaySummaryDayStrikeInfo.getCompletedDuoCardsSentenceTaskCount();
                    isDuoCardsSentenceTaskCompleted = completedDuoCardsSentenceTaskCount >= 5;

                    var deckNames = ankiService.getFilteredMajorDecks().block();
                    Map<String, AnkiDeckStats> deckStatsMap = ankiService.getDecksStats(deckNames).block();
                    deckStatsMap.entrySet().stream()
                            .forEach(entry ->
                            {
                                String deckName = entry.getKey();
                                var deckStats = ankiService.getDeckStats(deckName).block();
                                user.getDayStrikeInfoMap().get(today).getAnkiDeckStatsMap().put(deckName, deckStats);
                                var isDeckCompleted = deckStats.getLearnCount() == 0 && deckStats.getNewCount() == 0 && deckStats.getReviewCount() == 0;
                                deckInfoMap.put(deckName, isDeckCompleted);
                            });


                }

            }


            Boolean strikeDayResult;

            if (dayStrikeInfo != null) {
                strikeDayResult = isAllDayTasksCompleted(dayStrikeInfo);
            } else {
                strikeDayResult = null;
            }

            weekStrikeInfoMap.put(day, strikeDayResult);
            user.getDayStrikeInfoMap().put(day, dayStrikeInfo);
            day = day.minusDays(1L);
        }

        var weekStrikeInfoList = weekStrikeInfoMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> DayStrikeTemplateDate.builder()
                        .dayName(entry.getKey().getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()))
                        .isCompleted(entry.getValue())
                        .build()
                )
                .collect(Collectors.toList());

        var deckInfoList = deckInfoMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> DayDeckTemplateDate.builder()
                        .deckName(entry.getKey())
                        .isSuccesses(entry.getValue())
                        .build()
                )
                .collect(Collectors.toList());


        return DayUserStatisticsTemplateDate.builder()
                .completedPhysTaskCount(completedPhysTaskCount)
                .isPhysTaskCompleted(isPhysTaskCompleted)
                .completedDuoCardsSentenceTaskCount(completedDuoCardsSentenceTaskCount)
                .isDuoCardsSentenceTaskCompleted(isDuoCardsSentenceTaskCompleted)
                .deckListTemplateDate(deckInfoList)
                .strikeListTemplateDate(weekStrikeInfoList)
                .build();
    }


    private DayTaskCompletionInfo getDefaultDayStrikeInfo(User user, LocalDate day) {
        DayTaskCompletionInfo dayTaskCompletionInfo;
        Map<String, AnkiDeckStats> defaultDeckStrikeInfo = getDefaultDeckStrikeInfo();

        dayTaskCompletionInfo = DayTaskCompletionInfo.builder()
                .user(user)
                .day(day)
                .completedPhysTaskCount(0)
                .completedDuoCardsSentenceTaskCount(0)
                .ankiDeckStatsMap(defaultDeckStrikeInfo)
                .build();
        return dayTaskCompletionInfo;
    }

    private @NonNull Map<String, AnkiDeckStats> getDefaultDeckStrikeInfo() {
        List<String> deckList = ankiService.getFilteredMajorDecks().block();

        Map<String, AnkiDeckStats> defaultDeckStrikeInfo = new HashMap<>();

        var defaultAnkiDeckStats = AnkiDeckStats.builder()
                .learnCount(0)
                .newCount(0)
                .reviewCount(0)
                .build();

        deckList.stream().forEach(deckName -> defaultDeckStrikeInfo.put(deckName, defaultAnkiDeckStats));
        return defaultDeckStrikeInfo;
    }

    private boolean isAllDayTasksCompleted(DayTaskCompletionInfo dayTaskCompletionInfo) {
        return dayTaskCompletionInfo.getCompletedPhysTaskCount() >= 5
                && dayTaskCompletionInfo.getCompletedDuoCardsSentenceTaskCount() >= 5
                && isAllDecksCompleted(dayTaskCompletionInfo.getAnkiDeckStatsMap());
    }

    private boolean isAllDecksCompleted(Map<String, AnkiDeckStats> ankiDeckStatsMap) {
        for (Map.Entry<String, AnkiDeckStats> deckInfo : ankiDeckStatsMap.entrySet()) {
            var ankiDeckStats = deckInfo.getValue();
            var isDeckCompleted = ankiDeckStats.getLearnCount() == 0 && ankiDeckStats.getNewCount() == 0 && ankiDeckStats.getReviewCount() == 0;

            if (!isDeckCompleted) {
                return false;
            }
        }

        return true;
    }

    private static boolean isTodayOrDayBeforeToday(LocalDate day, LocalDate today) {
        return day.equals(today) || day.isBefore(today);
    }

    private static boolean isWeekDay(LocalDate day, LocalDate startOfWeek) {
        return !day.isBefore(startOfWeek);
    }
}
