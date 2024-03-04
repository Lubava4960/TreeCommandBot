package org.example.command;

import com.vdurmont.emoji.EmojiParser;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.config.DocumentDownloader;
import org.example.config.DocumentUploader;
import org.example.config.TelegramBotConfiguration;
import org.example.model.User;
import org.example.repository.CategoryRepository;
import org.example.repository.UserRepository;
import org.example.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;


@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Generated
@Setter
@Component

public  class TelegramBotListener extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    TelegramBotConfiguration config;
    @Autowired
    private Map<String, Command> commandList;
    private static final Map<Long, String> contextMap = new HashMap<>();
    @Autowired
    private AddCategory addCategory;
    private CategoryServiceImpl categoryService;


    @Autowired
    private DocumentUploader documentUploader;
    @Autowired
    private DocumentDownloader documentDownloader;


@PostConstruct
public void command(){
        commandList = new HashMap<>();
        commandList.put("/start", new StartCommand());
        commandList.put("/help", new HelpCommand());
        commandList.put("/addElement", new AddCategory());
        commandList.put("/viewTree", new ViewCommand());
        commandList.put("/removeTree", new RemoveCategory());
//        commandList.put("/download"),new  DocumentDownloader();
//        commandList.put("/upload"),new DocumentUploader();


    }


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
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
                    case "/help":
                    sendMessage(chatId, HelpCommand.HELP_TEXT);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognized! ");

                case "/addElement":
                    sendMessage(chatId, "Введите категорию ");

                       break;
                case "/viewTree":
                    sendMessage(chatId,String.valueOf(ViewCommand.class));
                       break;
                case "/removeTree":
                    sendMessage(chatId,String.valueOf(RemoveCategory.class));

                      break;
            }
        }

    }





    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void startCommandReceived(long chatId, String name) throws TelegramApiException {
        String answer= EmojiParser.parseToUnicode("Hi," + name+ " nice to meet you! " +" :blush:");
      // String answer = ("Hi," + name+ " nice to meet you! ") ;
       sendMessage(chatId,answer);

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

        }

    }


//    @Override
//   public void onUpdateReceived(Update update) {
//        UpdateReceiver updateReceiver = new UpdateReceiver(update);
//        Command command = commandList.get(updateReceiver.getCommand());
//        if (command != null) {
//
//            try {
//                command.execute(updateReceiver.getUpdate());
//            } catch (TelegramApiException e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//
//    }

    @Override
    public String getBotToken() {

        return config.getToken();
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }


}





