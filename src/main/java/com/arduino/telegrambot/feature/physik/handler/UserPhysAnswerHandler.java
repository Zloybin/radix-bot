package com.arduino.telegrambot.feature.physik.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.feature.physik.entity.PhysicsTaskDailyInfo;
import com.arduino.telegrambot.feature.physik.entity.PhysicsTaskDailyInfoId;
import com.arduino.telegrambot.feature.physik.service.PhysicsTaskDailyInfoService;
import com.arduino.telegrambot.feature.physik.service.PhysicsTaskService;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.feature.physik.service.ResultService;
import com.arduino.telegrambot.statistick.processor.PhysTaskInfoProcessor;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import com.arduino.telegrambot.feature.rad.validator.AnswerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@Order()
public class UserPhysAnswerHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private AnswerValidator answerValidator;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private PhysicsTaskService physicsTaskService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private PhysTaskInfoProcessor physicsTaskDailyInfo;

    @Autowired
    private PhysicsTaskDailyInfoService physicsTaskDailyInfoService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return UserState.WAIT_USER_PHYS_ANSWER.equals(userService.findById(userRequest.getChatId()).getState());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var userAnswer = userRequest.getHandler();

        var result = answerValidator.validatePhysAnswer(user, userAnswer);

        resultService.save(result);
        user.getResults().add(result);
        user.setState(UserState.FREE);
        var today = LocalDate.now();

        var physicsTaskDailyInfo = physicsTaskDailyInfoService.getByUserAndDate(user, today);

        if(physicsTaskDailyInfo == null){

            var physicsTaskDailyInfoId = PhysicsTaskDailyInfoId.builder()
                    .date(LocalDate.now(ZoneId.systemDefault()))
                    .userId(user.getId())
                    .build();
            physicsTaskDailyInfo = PhysicsTaskDailyInfo.builder()
                    .physicsTaskDailyInfoId(physicsTaskDailyInfoId)
                    .user(user)
                    .physicsTaskGoal(0)
                    .completedPhysicsTaskCount(0)
                    .build();
        }

        physicsTaskDailyInfo.setCompletedPhysicsTaskCount(physicsTaskDailyInfo.getCompletedPhysicsTaskCount() + 1);
        physicsTaskDailyInfoService.save(physicsTaskDailyInfo);
        userService.save(user);

        var physTask = physicsTaskService.findById(user.getPhysTaskId());

        var section = physTask.getSection().getRussianName();
        var title = physTask.getTitle();
        var taskNumber = physTask.getTaskNumber();
        var selfTaskNumber = physTask.getSelfTaskNumber();
        var taskLevel = physTask.getTaskLevel().getTitle();
        var taskText = physTask.getTaskText();
        var pageNumber = physTask.getPageNumber();

        var html = templateProcessor.processPhysUserResultMessageTemplate(result.isResult(), result.getPhysicsTask().getAnswer(), result.getUserAnswer(), section, title, taskNumber, selfTaskNumber, taskLevel, taskText, pageNumber);

        var keyboard = keyboardBuilder.buildCompletedPhysTaskWithCorrectMenu();

        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getMessageId());
        telegramService.editRichMessage(userRequest.getChatId(), user.getMessageId(), keyboard, html);
    }
}
