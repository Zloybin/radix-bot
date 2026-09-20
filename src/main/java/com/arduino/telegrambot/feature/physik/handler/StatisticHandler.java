package com.arduino.telegrambot.feature.physik.handler;

import com.arduino.telegrambot.ui.ProgressBarProcessor;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.Section;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.feature.physik.model.SectionProgress;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.ResultService;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatisticHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private ProgressBarProcessor progressBarProcessor;



    @Autowired
    private TemplateProcessor templateProcessor;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "statistics".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        List<SectionProgress> sectionProgresses = new ArrayList<>();
        for (Section section : Section.values()){
            var sectionTaskCount = taskService.getSectionTaskCount(section);
            var completedTaskCount = resultService.getCompletedTaskInSectionFromUserCount(userRequest.getChatId(), section.name());
            var failedTaskCount = resultService.getFailedTaskInSectionFromUserCount(userRequest.getChatId(), section.name());
            var progressBar = progressBarProcessor.createProgressBar(sectionTaskCount, completedTaskCount, failedTaskCount);
            var percentage = progressBarProcessor.calculateCompletionPercentage(sectionTaskCount, completedTaskCount, failedTaskCount);

            var sectionProgress = SectionProgress.builder()
                    .section(section.getRussianName())
                    .totalTasks(sectionTaskCount)
                    .completedTasks(completedTaskCount)
                    .failedTasks(failedTaskCount)
                    .progressBar(progressBar)
                    .completionPercentage(percentage)
                    .build();
            sectionProgresses.add(sectionProgress);
        }

        var text = templateProcessor.processStatisticTemplate(sectionProgresses);
        var keyboard = keyboardBuilder.buildBackToPhysTaskMenuFromStatistic();

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);
    }
}
