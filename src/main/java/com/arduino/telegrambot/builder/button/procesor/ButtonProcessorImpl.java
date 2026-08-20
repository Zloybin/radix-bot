package com.arduino.telegrambot.builder.button.procesor;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class ButtonProcessorImpl implements ButtonProcessor{
    @Override
    public InlineKeyboardButton renameButton(InlineKeyboardButton button, String newText) {
        var newButton = new InlineKeyboardButton();
        newButton.setCallbackData(button.getCallbackData());
        newButton.setText(newText);
        return newButton;
    }
}
