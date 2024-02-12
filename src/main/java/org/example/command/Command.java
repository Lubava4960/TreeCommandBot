package org.example.command;

import org.hibernate.sql.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Command {

    void execute(org.telegram.telegrambots.meta.api.objects.Update update) throws TelegramApiException;


    void execute(Update update) throws TelegramApiException;
}
