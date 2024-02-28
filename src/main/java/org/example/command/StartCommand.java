package org.example.command;

import com.vdurmont.emoji.EmojiParser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.config.TelegramBotListener;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;

@EqualsAndHashCode()
@Slf4j
@Data
@Generated
@Setter
@Component

public class StartCommand implements org.example.command.Command {

    private TelegramBotListener telegramBotListener;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void execute(Update update) throws TelegramApiException {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText=update.getMessage().getText();
            long chatId=update.getMessage().getChatId();
            switch (messageText){
                    case "/start":
                        registerUser(update.getMessage());
                        try {
                            startCommandReceived(chatId, update.getMessage().getChat().getFirstName());

                        } catch (TelegramApiException e) {
                            throw new RuntimeException(e);
                        }

                        break;
                    default:
                        telegramBotListener.sendMessage(chatId, "Sorry, command was not recognized! ");

                }

            }


        }

    private void registerUser(Message msg) {
        if(userRepository.findById(msg.getChatId()).isEmpty()){
            var chatId=msg.getChatId();
            var chat=msg.getChat();
            User user= new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info("user saved: "+user);
        }

    }


    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public void execute() {

    }





    private void execute(SendMessage message) {


        }





    public void startCommandReceived(long chatId, String name) throws TelegramApiException {
        String answer = EmojiParser.parseToUnicode("Hi," + name + " nice to meet you! " + ":blush:");
        //log.info("replied to user " + name);
        telegramBotListener.sendMessage(chatId, answer);
    }

}
