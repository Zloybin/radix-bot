package com.arduino.telegrambot.handle.anki;

import com.arduino.telegrambot.anki.AnkiCurrentCard;
import com.arduino.telegrambot.anki.AnkiService;
import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.User;
import com.arduino.telegrambot.enummeration.AnkiAnswer;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.handle.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.TelegramService;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;

@Component
public class AnkiAnswerProcessorHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiService ankiService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());

        return UserState.WAIT_ANKI_ANSWER.equals(user.getState());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.FREE);
        userService.save(user);

        AnkiCurrentCard currentCard = null;
        for (AnkiAnswer value : AnkiAnswer.values()) {
            if(value.name().equalsIgnoreCase(userRequest.getRequest())){
                Boolean showAnswer = ankiService.showAnswer().block();
                System.out.println("showAnswer: " + showAnswer);

                currentCard = ankiService.answerAndGetNextCard(value.getValue()).block();
                break;
            }
        }


        var isStarted = ankiService.startStudy(userRequest.getRequest()).block();
        var isShowQuestion = ankiService.showQuestion().block();

        var keyboard = keyboardBuilder.buildAnkiShowAnswerKeyboard();

        var text = templateProcessor.processFrontCardTemplate(currentCard);


        telegramService.editMessage(userRequest.getChatId(), userRequest.getMessageId(), text, keyboard, ParseMode.HTML);

    }
}
