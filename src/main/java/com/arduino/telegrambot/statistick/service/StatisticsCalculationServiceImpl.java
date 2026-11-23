package com.arduino.telegrambot.statistick.service;

import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.model.DailyTaskInfo;
import com.arduino.telegrambot.update.TaskStatusService;
import com.arduino.telegrambot.entity.Task;
import com.arduino.telegrambot.update.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsCalculationServiceImpl implements StatisticsCalculationService {

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<DailyTaskInfo> getDailyTaskInfoList(User user, LocalDate date) {

        List<DailyTaskInfo> dailyTaskInfoList = new ArrayList<>();

        var userActiveTaskInfoList = taskRepository.findByRequiredForStrikeAndActive(true, true);
        for (Task task : userActiveTaskInfoList) {
            var status = taskStatusService.getStatus(user, task.getTaskType(), date);
            var dailyTaskInfo = DailyTaskInfo.builder()
                    .taskStatus(status)
                    .taskType(task.getTaskType())
                    .taskDescription(task.getTaskType().getDescription())
                    .build();
            dailyTaskInfoList.add(dailyTaskInfo);
        }
        return dailyTaskInfoList;
    }

    @Override
    public List<DailyTaskInfo> getTodayDailyTaskInfoList(User user) {
        var today = LocalDate.now(ZoneId.systemDefault());
        return getDailyTaskInfoList(user, today);
    }
}
