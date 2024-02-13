package org.example.config;

import com.vdurmont.emoji.EmojiParser;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Generated
@Setter
@Component

public  class TelegramBotListener extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;


    final TelegramBotConfiguration config;


    static final String HELP_TEXT="This bot add category tree, delete element category, shows category tree \n\n"+
            "you can execute commands from the main menu on the left or by typing a command \n\n"+
            "Type /start to see a welcome message \n\n"+
            "Type /help to see info \n\n"+
            "Type /addRoot to see a add Root category \n\n"+
            "Type /addCategory to see a add child category \n\n"+
            "Type /viewCategory to see a category tree \n\n"+
            "Type /removeElement element category you mast  a remove element" ;
    private org.hibernate.sql.Update update;


    public TelegramBotListener (TelegramBotConfiguration config) {

        this.config = config;

        List<BotCommand> listOfCommands=new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/help","info how to use this bot"));
        listOfCommands.add(new BotCommand("/addRoot","add Root Category"));
        listOfCommands.add(new BotCommand("/addCategory","add child category"));
        listOfCommands.add(new BotCommand("/viewCategory","shows category tree"));

        try{
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(),null));
        }catch (TelegramApiException e){

            log.error("Error setting bot command list");

        }

        }

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
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "/addRoot":
                    addRoot(chatId);
                    break;

                default:
                    sendMessage(chatId, "Sorry, command was not recognized! ");

            }
        }else if (update.hasCallbackQuery()){
            String callbackData=update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (callbackData.equals("YES_BUTTON")){
                String text = "You have added a root category";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId((int)messageId);

                try {
                    execute(message);
                }catch (TelegramApiException e){
                    log.error("Error occurred: "+ e.getMessage());
                }
            }else if(callbackData.equals("NO_BUTTON")){
                String text = "You haven't added a root category";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId((int)messageId);

                try {
                    execute(message);
                }catch (TelegramApiException e){
                    log.error("Error occurred: "+ e.getMessage());
                }

            }

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

