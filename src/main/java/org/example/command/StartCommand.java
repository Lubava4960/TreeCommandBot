package org.example.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@EqualsAndHashCode()
@Slf4j
@Data
@Generated
@Setter
@Component

public class StartCommand implements org.example.command.Command  {

    private TelegramBotListener telegramBotListener;

    @Autowired
    private UserRepository userRepository;



    public  void sendMessage(long chatId, String textToSend){
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        execute();
    }

    private void execute() {
    }


    @Override
    public void execute(Update update) throws TelegramApiException {
//        if(update.hasMessage() && update.getMessage().hasText()){
//            String messageText=update.getMessage().getText();
//            long chatId=update.getMessage().getChatId();
//            switch (messageText){
//                case "/start":
//                    registerUser(update.getMessage());
//
//                    try {
//                        startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
//                    } catch (TelegramApiException e) {
//                        throw new RuntimeException(e);
//                    }
//
//                    break;
//                default:
//                    sendMessage(chatId, "Sorry, command was not recognized! ");
//
//
//            }
//        }


    }

//    private  void registerUser(Message msg) {
//        if(userRepository.findById(msg.getChatId()).isEmpty()){
//            var chatId=msg.getChatId();
//            var chat=msg.getChat();
//            User user= new User();
//            user.setChatId(chatId);
//            user.setFirstName(chat.getFirstName());
//            user.setLastName(chat.getLastName());
//            user.setUserName(chat.getUserName());
//            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
//            userRepository.save(user);
//
//        }
//
//    }

    @Override
    public String getCommand() {
        return "/start";
    }




//    public void startCommandReceived(long chatId, String firstName) throws TelegramApiException {
//        String answer = ("Hi," + getTelegramBotListener().getBotUsername()+ " nice to meet you! ") ;
//
//        sendMessage(chatId,answer);
//
//    }



}






