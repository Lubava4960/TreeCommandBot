package org.example.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.command.*;
import org.example.model.User;
import org.example.repository.CategoryRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Generated
@Setter
@Component

public  class TelegramBotListener extends TelegramLongPollingBot{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    TelegramBotConfiguration config;
    @Autowired
    private Map<String, Command> commandList;
    private static final Map<Long, String> contextMap = new HashMap<>();
    @Autowired
    private DocumentUploader documentUploader;
    @Autowired
    private DocumentDownloader documentDownloader;


    private org.hibernate.sql.Update update;


    @PostConstruct
    public void command ( ) {

        commandList=new HashMap<>();
        commandList.put("/start", new StartCommand());
        commandList.put("/help", new HelpCommand());
        commandList.put("/addElement", new AddCategory());
        commandList.put("/viewTree", new ViewCommand());
        commandList.put("/removeTree", new RemoveCategory());
        commandList.put("/download", new DocumentDownloader());
        commandList.put("uploader", new DocumentUploader());


    }


            public void onUpdateReceived(Update update)  {
                String answer = "";
                UpdateReceiver updateReceiver = new UpdateReceiver(update);
                Command command = commandList.get(update.getMessage().getText());
                command.execute();
                if (updateReceiver.getCommand().equals("/download"))sendDocument(updateReceiver);
                else {
                    sendMessage(updateReceiver.getUserId(),answer);
                }


            }

    private void sendDocument(UpdateReceiver updateReceiver) {
        // Получение информации о документе из updateReceiver
        String documentUrl = updateReceiver.getDocumentUrl();
        String caption = updateReceiver.getDocumentCaption();

        // Отправка документа пользователю
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(updateReceiver.getUserId());
        //sendDocument.setDocument(documentUrl);
        sendDocument.setCaption(caption);

        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending document: " + e.getMessage());
        }
    }



    public   void registerUser(Message msg) {
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



    public  void sendMessage(long chatId, String textToSend){
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
//        ReplyKeyboardMarkup keyboardMarkup=new ReplyKeyboardMarkup();
//        List<KeyboardRow> keyboardRows=new ArrayList<>();
//        KeyboardRow row = new KeyboardRow();
//        row.add("/help");
//        row.add("/addCategory");
//        keyboardRows.add(row);
//
//        row= new KeyboardRow();
//
//        row.add("/viewCategory");
//        row.add("/removeElement");
//        keyboardRows.add(row);
//        keyboardMarkup.setKeyboard(keyboardRows);
//        message.setReplyMarkup(keyboardMarkup);

        try {
            execute(message);
        }catch (TelegramApiException e){
            log.error("Error occurred: "+ e.getMessage());
        }

    }

    @Override
    public String getBotToken(){

        return config.getToken();
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }



    @PostConstruct
    public void init() {
        // Логика инициализации
    }

    @PreDestroy
    public void destroy() {
        // Логика завершения работы
    }



}

