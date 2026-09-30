package com.arduino.telegrambot.feature.duocards.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.enummeration.UserState;
import com.arduino.telegrambot.feature.ai.LLMService;
import com.arduino.telegrambot.feature.anki.service.AnkiServiceImpl;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddDuoCardHandler implements UpdateHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private AnkiServiceImpl ankiService;

    @Autowired
    private LLMService llmService;



    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "addNewNote".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {

        var user = userService.findById(userRequest.getChatId());
        user.setState(UserState.ADD_NEW_NOTE_MODE);
        user.setMessageId(userRequest.getMessageId());
        userService.save(user);

        var keyboard = keyboardBuilder.buildCancelAddNewNoteKeyboard();
        var text = templateProcessor.processAddNewNoteMessageTemplate();
        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);


//        Note note = Note.builder()
//                .deckName("Deutsch")
//                .modelName("Deutsch (с обратной карточкой)")
//                .field("Front", "Чем ArrayList отличается от LinkedList?")
//                .field("Back", "ArrayList: массив, O(1) доступ по индексу. LinkedList: двусвязный список.")
//                .field("Beispiel", "ArrayList: массив, O(1) доступ по индексу. LinkedList: двусвязный список.")
//                .tag("java")
//                .options(Options.builder().allowDuplicate(false).build())
//                .build();
//
//        ankiService.addNote(note)
//                .doOnNext(id -> System.out.println("Создана заметка: " + id))
//                .doOnError(e -> System.err.println("Ошибка Anki: " + e.getMessage()))
//                .subscribe();
    }
}
