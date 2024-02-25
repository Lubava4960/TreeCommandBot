package org.example.command;

import com.vdurmont.emoji.EmojiParser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.config.TelegramBotListener;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@EqualsAndHashCode()
@Slf4j
@Data
@Generated
@Setter
@Component

public class StartCommand implements org.example.command.Command {

    private TelegramBotListener telegramBotListener;

    public StartCommand() {
        this.telegramBotListener = telegramBotListener;
    }


    @Autowired
    private UserRepository userRepository;




    @Override
    public void execute(Update update) throws TelegramApiException {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                switch (messageText) {
                    case "/start":
                        telegramBotListener.registerUser(update.getMessage());
                        try {
                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());

                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }

                        break;
                    default:
                        sendMessage(chatId, "Sorry, command was not recognized! ");

                }

            }


        }




    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public void execute() {

    }


    private void sendMessage(long chatId, String s) {
    }

    public void startCommandReceived(long chatId, String name) throws TelegramApiException {
        String answer = EmojiParser.parseToUnicode("Hi," + name + " nice to meet you! " + ":blush:");
        log.info("replied to user " + name);
        sendMessage(chatId, answer);
    }

}
