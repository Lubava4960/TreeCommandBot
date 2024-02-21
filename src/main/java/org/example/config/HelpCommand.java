package org.example.config;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class HelpCommand implements org.example.command.Command {
    @Override
    public void execute(Update update) throws TelegramApiException {

    }

    @Override
    public void execute(org.hibernate.sql.Update update) throws TelegramApiException {

    }

    @Override
    public String getCommand() {
        return null;
    }
}
