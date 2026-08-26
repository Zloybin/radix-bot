package com.arduino.telegrambot.handle.physik;

import com.arduino.telegrambot.ProgressBarProcessor;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.Result;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.Section;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.SectionProgress;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.ResultService;
import com.arduino.telegrambot.service.TaskService;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatisticHandler implements UpdateHandler {

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;

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
        return "statistics".equals(userRequest.getRequest());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());

        List<SectionProgress> sectionProgresses = new ArrayList<>();
        for (Section section : Section.values()){
            var sectionTaskCount = taskService.getSectionTaskCount(section);
            var completedTaskCount = resultService.getCompletedTaskInSectionFromUserCount(userRequest.getChatId(), section.name());
            var failedTaskCount = resultService.getFailedTaskInSectionFromUserCount(userRequest.getChatId(), section.name());
            var progressBar = progressBarProcessor.createProgressBar(sectionTaskCount, completedTaskCount, failedTaskCount);

            var sectionProgress = SectionProgress.builder()
                    .section(section.getRussianName())
                    .totalTasks(sectionTaskCount)
                    .completedTasks(completedTaskCount)
                    .failedTasks(failedTaskCount)
                    .progressBar(progressBar)
                    .build();
            sectionProgresses.add(sectionProgress);
        }

        var text = templateProcessor.processStatisticTemplate(sectionProgresses);
        var keyboard = keyboardBuilder.buildBackToPhysTaskMenuFromStatistic();

        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);
    }
}
