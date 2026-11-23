package com.arduino.telegrambot.feature.duocards.sentence.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.feature.ai.LLMService;
import com.arduino.telegrambot.feature.duocards.sentence.entity.DuoCardsSentenceDailyInfo;
import com.arduino.telegrambot.feature.duocards.sentence.model.GeneratedExample;
import com.arduino.telegrambot.feature.duocards.sentence.service.DuoCardsSentenceDailyInfoService;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.statistick.processor.DuoCardsInfoProcessor;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ProcessUserAnswerSentenceExerciseHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private LLMService llmService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private DuoCardsInfoProcessor duoCardsInfoProcessor;

    @Autowired
    private DuoCardsSentenceDailyInfoService duoCardsSentenceDailyInfoService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        return UserState.WAIT_EXERCISE_SENTENCE_ANSWER.equals(user.getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var generatedExample = user.getGeneratedExample();

        String handler = userRequest.getHandler();
        var answerCheckResult = llmService.checkAnswer(generatedExample.getGeneratedSourceExample(), generatedExample.getGeneratedTranslation(), generatedExample.getGeneratedSentence(), handler);

        var updatedGeneratedExample = GeneratedExample.builder()
                .generatedSentence("")
                .generatedSourceExample("")
                .generatedTranslation("")
                .build();

        var text = templateProcessor.processDuoCardResultSentenceExercise(answerCheckResult, generatedExample, handler);
        var keyboard = keyboardBuilder.buildResultSentenceExerciseKeyboard();
//        templateProcessor.processResultSentenceExercise(answerCheckResult.getCorrectedSentence(),
//                answerCheckResult.getFeedback(),
//                answerCheckResult.);

        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getMessageId());
        telegramService.editRichMessage(userRequest.getChatId(), user.getMessageId(), keyboard, text);

        user.setGeneratedExample(updatedGeneratedExample);
        user.setState(UserState.FREE);
        user.setMessageId(0L);
        var today = LocalDate.now();

        //-------------------


        DuoCardsSentenceDailyInfo duoCardsSentenceDailyInfo = duoCardsSentenceDailyInfoService.getByUserAndDate(user, today);


        if(duoCardsSentenceDailyInfo == null){
            duoCardsSentenceDailyInfo = DuoCardsSentenceDailyInfo.builder()
                    .completedDuoCardsSentenceTaskCount(0)
                    .duoCardsSentenceGoal(0)
                    .build();
        }

        duoCardsSentenceDailyInfo.setCompletedDuoCardsSentenceTaskCount(duoCardsSentenceDailyInfo.getCompletedDuoCardsSentenceTaskCount() + 1);

        duoCardsSentenceDailyInfoService.save(duoCardsSentenceDailyInfo);

    }
}
