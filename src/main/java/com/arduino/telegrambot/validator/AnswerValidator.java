package com.arduino.telegrambot.validator;

import com.arduino.telegrambot.converter.RadConverter;
import com.arduino.telegrambot.enummeration.NumberSystem;
import com.arduino.telegrambot.model.TaskResult;
import lombok.RequiredArgsConstructor;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@RequiredArgsConstructor
public class AnswerValidator {

    @Autowired
    private final RadConverter radConverter;

    public TaskResult validateAnswer(String task, String answer) {

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
            var decimal = Integer.valueOf(taskValue);
            rightAnswer = radConverter.convertBinaryToDecimal(decimal);
        } else if (NumberSystem.BIN.equals(source) && NumberSystem.HEX.equals(target)) {
            var decimal = Integer.valueOf(taskValue);
            rightAnswer = radConverter.convertBinaryToHex(decimal);
        } else {
            throw new IllegalArgumentException(String.format("Определен неправильная конвертация: невозможно конвкртировать из %s в %s.", source, target));
        }

        boolean result = rightAnswer.equals(answer);


        return TaskResult.builder()
                .result(result)
                .userAnswer(answer)
                .rightAnswer(rightAnswer)
                .build();
    }
}
