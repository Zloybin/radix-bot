package com.arduino.telegrambot;

import org.springframework.stereotype.Component;

@Component
public class ProgressBarProcessor {

    public String createProgressBar(int total, int correct, int incorrect) {
        int segments = 15;

        if (total == 0) {
            return "⬜".repeat(segments);
        }

        int purple = Math.round((float) correct / total * segments);
        int orange = Math.round((float) incorrect / total * segments);

        if (purple + orange > segments) {
            orange = segments - purple;
        }

        int empty = segments - purple - orange;

        return "🟧".repeat(purple)
                + "🟪".repeat(orange)
                + " ▪︎ ".repeat(empty);
    }

    public int calculateCompletionPercentage(
            long totalTasks,
            long completedTasks,
            long failedTasks
    ) {
        if (totalTasks == 0) {
            return 0;
        }

        return (int) Math.round(
                (double) (completedTasks + failedTasks)
                        / totalTasks
                        * 100);
    }
}
