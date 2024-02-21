package org.example.config;

import com.vdurmont.emoji.EmojiParser;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.command.Command;
import org.example.model.Category;
import org.example.model.User;
import org.example.repository.CategoryRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Generated
@Setter
@Component

public  class TelegramBotListener extends TelegramLongPollingBot {
    private String name;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    TelegramBotConfiguration config;
    @Autowired
    private Map<String,Command> commandList;
    private static final Map<Long,String> contextMap=new HashMap<>();
    @Autowired
    private DocumentUploader documentUploader;
    @Autowired
    private DocumentDownloader documentDownloader;

    static final String HELP_TEXT="This bot add category tree, delete element category, shows category tree \n\n"+
            "you can execute commands from the main menu on the left or by typing a command \n\n"+
            "Type /start to see a welcome message \n\n"+
            "Type /help to see info \n\n"+
            "Type /addRoot to see a add Root category \n\n"+
            "Type /addCategory to see a add child category \n\n"+
            "Type /viewCategory to see a category tree \n\n"+
            "Type /removeElement element category you mast  a remove element" ;
    private org.hibernate.sql.Update update;


    @PostConstruct
    public void TelegramBotListener ( TelegramBotConfiguration config) {
        this.config = config;
        commandList=new HashMap<>();
      commandList.put("/start", new StartCommand());
      commandList.put("/help", new HelpCommand());
      commandList.put("/addRoot", new AddRoot());
      commandList.put("/addCategory", new ChildCategory());
      commandList.put("/viewCategory", new ViewCategory());
      commandList.put("/removeCategory", new RemoveCategory());

    }

    @Override
    public synchronized void onUpdateReceived(Update update) {

        String lastMessage = contextMap.put(Update.getMessage()
                        .getFrom()
                        .getId(),
                update.getMessage()
                        .getText());
        boolean documentUploadWaiting = Object.equals(lastMessage, "/upload");
        if (documentUploadWaiting) {
            onUpdateReceivedDocument(update);
        } else {
            onUpdateReceived(update);
        }
    }

        public void onUpdateReceived(Update update)  {
        String answer;
        UpdateReceiver updateReceiver = new UpdateReceiver(update);
        try {
            Command command = new commandList.get(update.getMessage().getText());
            command.execute();
        }catch (TelegramApiException e){
            log.error("Error occurred: "+ e.getMessage());
        }
        if (updateReceiver.getCommand().equals("/download"))sendDocument(updateReceiver);
        else {
            sendMessage(updateReceiver.getUserId(),answer);
        }

    }


    private void onUpdateReceivedDocument(Update update) throws TelegramApiException {
        String answer;
        if ((update.getMessage().hasDocument())){
            String filed =update.getMessage().getDocument().getFileId();
            GetFile getFile=new GetFile();
            getFile.setFileId(filed);
            String filePath=execute(getFile).getFilePath();
            String file;
            file = File.getFileUrl("tempFile", "xlsx");
            downloadFile(filePath, new java.io.File(file));
            answer=documentUploader.upload(file);

        }
    }



   private void addRoot(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Do you want to add a root category? ");
       InlineKeyboardMarkup markupInline=new InlineKeyboardMarkup();
       List<List<InlineKeyboardButton>> rowsInline= new ArrayList<>();
       List<InlineKeyboardButton>rowInline=new ArrayList<>();
       var yesButton = new InlineKeyboardButton();

       yesButton.setText("Yes");
       yesButton.setCallbackData("YES_BUTTON");

       var noButton = new InlineKeyboardButton();
       noButton.setText("No");
       noButton.setCallbackData("NO_BUTTON");

       rowInline.add(yesButton);
       rowInline.add(noButton);

       rowsInline.add(rowInline);
       markupInline.setKeyboard(rowsInline);
       message.setReplyMarkup(markupInline);
       try {
           execute(message);
       }catch (TelegramApiException e){
           log.error("Error occurred: "+ e.getMessage());
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

    public void startCommandReceived(long chatId, String name) throws TelegramApiException {

        String answer= EmojiParser.parseToUnicode("Hi," + name+ " nice to meet you! "+":blush:");
        log.info("replied to user "+ name);
        sendMessage(chatId,answer);

    }
    public void saveRoot(long chatId, String name)throws TelegramApiException{
        String answer= EmojiParser.parseToUnicode("add Root"+":blush");
        log.info("add Root ");

        sendMessage(chatId,answer);
    }


    public  void sendMessage(long chatId, String textToSend){
        SendMessage message=new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        ReplyKeyboardMarkup keyboardMarkup=new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows=new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("/addRoot");
        row.add("/addCategory");
        keyboardRows.add(row);

        row= new KeyboardRow();

        row.add("/viewCategory");
        row.add("/removeElement");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

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
}

