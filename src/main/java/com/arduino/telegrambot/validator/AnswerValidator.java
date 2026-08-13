package com.arduino.telegrambot.validator;

import com.arduino.telegrambot.converter.RadConverter;
import com.arduino.telegrambot.entity.Result;
import com.arduino.telegrambot.enummeration.NumberSystem;
import com.arduino.telegrambot.model.RadTaskResult;
import com.arduino.telegrambot.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerValidator {

    @Autowired
    private final RadConverter radConverter;

    @Autowired
    private final TaskRepository taskRepository;

    public RadTaskResult validateAnswer(String task, String answer) {

//        validateTask

        var source = NumberSystem.valueOf(task.substring(0, 3));
        var target = NumberSystem.valueOf(task.substring(3, 6));
        var taskValue = task.substring(6);

        String rightAnswer;

        if (NumberSystem.DEC.equals(source) && NumberSystem.HEX.equals(target)) {
            Integer decimal = Integer.valueOf(taskValue);
            rightAnswer = radConverter.convertDecimalToHex(decimal);
        } else if (NumberSystem.DEC.equals(source) && NumberSystem.BIN.equals(target)) {
            var decimal = Integer.valueOf(taskValue);
            rightAnswer = radConverter.convertDecimalToBinary(decimal);
        } else if (NumberSystem.HEX.equals(source) && NumberSystem.DEC.equals(target)) {
            radConverter.convertHexToDecimal(taskValue);
            rightAnswer = String.valueOf(radConverter.convertHexToDecimal(taskValue));
        } else if (NumberSystem.HEX.equals(source) && NumberSystem.BIN.equals(target)) {
            rightAnswer = radConverter.convertHexToBinary(taskValue);
        } else if (NumberSystem.BIN.equals(source) && NumberSystem.DEC.equals(target)) {
            var binary = Integer.valueOf(taskValue);
            rightAnswer = radConverter.convertBinaryToDecimal(binary);
        } else if (NumberSystem.BIN.equals(source) && NumberSystem.HEX.equals(target)) {
            var binary = Integer.valueOf(taskValue);
            rightAnswer = radConverter.convertBinaryToHex(binary);
        } else {
            throw new IllegalArgumentException(String.format("Определен неправильная конвертация: невозможно конвкртировать из %s в %s.", source, target));
        }

        boolean result = rightAnswer.equals(answer);


        return RadTaskResult.builder()
                .result(result)
                .userAnswer(answer)
                .rightAnswer(rightAnswer)
                .build();
    }

    public Result validatePhysAnswer(Long taskId, String answer) {

        var task = taskRepository.findById(taskId).get();
        var rightAnswer = task.getAnswer();

        return Result.builder()
                .result(answer.equals(rightAnswer))
                .userAnswer(answer)
                .task(task)
                .build();
    }
}
