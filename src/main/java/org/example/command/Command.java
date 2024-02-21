package org.example.command;

import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
public interface Command {

    void execute(org.telegram.telegrambots.meta.api.objects.Update update) throws TelegramApiException;


    void execute(Update update) throws TelegramApiException;
    String getCommand();


    void execute();
}
