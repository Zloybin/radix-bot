package com.arduino.telegrambot.feature.duocards.handler;

import com.arduino.telegrambot.builder.keyboard.KeyboardBuilder;
import com.arduino.telegrambot.entity.DuoCardExample;
import com.arduino.telegrambot.feature.anki.model.Note;
import com.arduino.telegrambot.feature.anki.model.Options;
import com.arduino.telegrambot.feature.anki.service.AnkiService;
import com.arduino.telegrambot.feature.duocards.model.DuoCard;
import com.arduino.telegrambot.feature.duocards.service.DuoCardsService;
import com.arduino.telegrambot.handler.UpdateHandler;
import com.arduino.telegrambot.model.UserRequest;
import com.arduino.telegrambot.service.UserService;
import com.arduino.telegrambot.telegram.TelegramService;
import com.arduino.telegrambot.ui.template.TemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SaveNewDuoCardHandler implements UpdateHandler {

    @Autowired
    private AnkiService ankiService;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyboardBuilder keyboardBuilder;

    @Autowired
    private TemplateProcessor templateProcessor;

    @Autowired
    private TelegramService telegramService;

    @Autowired
    private DuoCardsService duoCardsService;

    @Override
    public boolean isApplicable(UserRequest userRequest) {
        return "saveDuoCard".equals(userRequest.getHandler());
    }

    @Override
    public void handle(UserRequest userRequest) {
        var user = userService.findById(userRequest.getChatId());
        var duoCardExample = user.getDuoCardExample();


        String sentence = duoCardExample.getSentence();
        String translation = duoCardExample.getTranslation();
        String example = duoCardExample.getExample();
        Note note = Note.builder()
                .deckName("Test")
                .modelName("Deutsch (с обратной карточкой)")
                .field("Front", sentence)
                .field("Back", translation)
                .field("Beispiel", example)
                .tag("")
                .options(Options.builder().allowDuplicate(false).build())
                .build();

        DuoCard card = duoCardsService.createCard(sentence, translation, example);

        ankiService.addNote(note)
                .doOnNext(id -> System.out.println("Создана заметка: " + id))
                .doOnError(e -> System.err.println("Ошибка Anki: " + e.getMessage()))
                .subscribe();

        user.setDuoCardExample(DuoCardExample.builder().build());
        userService.save(user);

        var keyboard = keyboardBuilder.buildPreviewSaveDuoCardKeyboard();

        var text = templateProcessor.processPreviewSavedDuoCard(sentence, translation, example);

        telegramService.editRichMessage(userRequest.getChatId(), userRequest.getMessageId(), keyboard, text);

    }
}
