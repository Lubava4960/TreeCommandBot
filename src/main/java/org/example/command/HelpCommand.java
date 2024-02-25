package org.example.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.config.TelegramBotListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@EqualsAndHashCode()
@Slf4j
@Data
@Generated
@Setter
@Component
public class HelpCommand implements org.example.command.Command {

    private TelegramBotListener telegramBotListener;
    static final String HELP_TEXT="This bot add category tree, delete element category, shows category tree \n\n"+
            "you can execute commands from the main menu on the left or by typing a command \n\n"+
            "Type /start to see a welcome message \n\n"+
            "Type /help to see info \n\n"+
            "Type /addElement to see a add Element of category \n\n"+
            "Type /viewTree to see a category tree \n\n"+
            "Type /removeTree \n\n"+
            "Type /download download document  \"n\n"+
            "Type /upload upload document \n\n"+
            "Type /removeElement element category you mast  a remove element" ;



    @Override
    public void execute(org.telegram.telegrambots.meta.api.objects.Update update) throws TelegramApiException {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                                case "/help":
                   telegramBotListener.sendMessage(chatId, HELP_TEXT);


                    break;
                default:
                    telegramBotListener.sendMessage(chatId, "Sorry, command was not recognized! ");

            }

        }


    }


    @Override
    public String getCommand() {
        return "/help";
    }

    @Override
    public void execute() {

    }


}
