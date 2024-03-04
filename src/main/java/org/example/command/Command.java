package org.example.command;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public interface Command {

    void execute(Update update) throws TelegramApiException;

    String getCommand();
}













