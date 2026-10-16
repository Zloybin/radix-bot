package com.arduino.telegrambot.statistick.processor;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.statistick.model.DayUserStatisticsTemplateDate;
import org.springframework.stereotype.Component;

@Component
public interface StatisticsProcessor {
    DayUserStatisticsTemplateDate getUserStatistics(User user);
}
