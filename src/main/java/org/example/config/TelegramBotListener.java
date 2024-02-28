package org.example.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.command.*;
import org.example.repository.CategoryRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
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
    @Autowired
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
    public void init() {

        commandList = new HashMap<>();
        commandList.put("/start", new StartCommand());
        commandList.put("/help", new HelpCommand());
        commandList.put("/addElement", new AddCategory());
        commandList.put("/viewTree", new ViewCommand());
        commandList.put("/removeTree", new RemoveCategory());

    }



    public void onUpdateReceived(Update update)  {
                UpdateReceiver updateReceiver = new UpdateReceiver(update);
                Command command = commandList.get(updateReceiver.getCommand());
                if (command != null) {

                    try {
                        command.execute(updateReceiver.getUpdate());
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }

                }
                }
//                String answer = "";
//                command.execute();
//                if (updateReceiver.getCommand().equals("/download"))sendDocument(updateReceiver);
//                else {
//                   sendMessage(updateReceiver.getUserId(),answer);





//    private void sendDocument(UpdateReceiver updateReceiver) {
//        // Получение информации о документе из updateReceiver
//        String documentUrl = updateReceiver.getDocumentUrl();
//        String caption = updateReceiver.getDocumentCaption();
//
//        // Отправка документа пользователю
//        SendDocument sendDocument = new SendDocument();
//        sendDocument.setChatId(updateReceiver.getUserId());
//        //sendDocument.setDocument(documentUrl);
//        sendDocument.setCaption(caption);
//
//        try {
//            execute(sendDocument);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public  void sendMessage(long chatId, String textToSend){
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
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







}

