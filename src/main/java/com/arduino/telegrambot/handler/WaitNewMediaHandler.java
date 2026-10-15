package com.arduino.telegrambot.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.TelegramMedia;
import com.arduino.telegrambot.enummeration.TelegramMediaType;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramMediaService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WaitNewMediaHandler implements UpdateHandler{

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramMediaService telegramMediaService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        return UserState.WAIT_NEW_MEDIA.equals(user.getState());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());

        var fileId = userRequest.getRequest();
        var key = userRequest.getHandler();
        var media = TelegramMedia.builder()
                .mediaType(TelegramMediaType.PHOTO)
                .fileId(fileId)
                .key(key)
                .build();

        telegramMediaService.save(media);

        var text = templateProcessor.processSuccessSaveMediaMessage(fileId, key);
        var keyboard = keyboardBuilder.buildBackToAdminMenu();
        telegramService.deleteMessage(userRequest.getChatId(), userRequest.getMessageId());
        telegramService.editRichMessage(userRequest.getChatId(), user.getMessageId(), keyboard, text);
        user.setState(UserState.FREE);
        user.setMessageId(0L);
        userService.save(user);

    }
}
