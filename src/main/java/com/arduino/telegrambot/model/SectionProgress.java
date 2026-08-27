package com.arduino.telegrambot.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SectionProgress {

    private final String section;
    private final String progressBar;
    private final long totalTasks;
    private final long completedTasks;
    private final long failedTasks;
    private final int completionPercentage;

    public SectionProgress(
            String section,
            String progressBar,
            long totalTasks,
            long completedTasks,
            long failedTasks,
            int completionPercentage
    ) {
        this.section = section;
        this.progressBar = progressBar;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.failedTasks = failedTasks;
        this.completionPercentage = completionPercentage;

    }
}
