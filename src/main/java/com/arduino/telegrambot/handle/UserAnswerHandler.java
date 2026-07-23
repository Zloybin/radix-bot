package com.arduino.telegrambot.handle;

import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAnswerHandler implements UpdateHandler{

    @Autowired
    private UserService userService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {

        return !userRequest.getRequest().startsWith("/") && UserState.TASK.equals(userService.getUser(userRequest.getChatId()).getState());
    }

    @Override
    public void handle(UserRequest userRequest) {

    }
}
