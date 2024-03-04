package org.example.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.CategoryRepository;
import org.example.service.CategoryServiceImpl;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс для добавления категорий при вызове команды /
 /addElement.
 */
@EqualsAndHashCode()
@Slf4j
@Data
@Generated
@Setter
@Component
public class AddCategory implements Command{
    private  CategoryRepository categoryRepository;
    private  TelegramBotListener telegramBotListener;
    private static CategoryServiceImpl categoryService;
    private org.telegram.telegrambots.meta.api.objects.Update update;


    /**
     * Метод для выполнения команды /addCategory.
     * Отправляет сообщение о добавлении элемента или об ошибке, если пользователь неверно оформил команду
     */
@Override
    public void execute(Update update) throws TelegramApiException {
    long chatId = update.getMessage().getChatId();
    String text = update.getMessage().getText();
    String[] commandArray = text.trim().split("\\s+");

    String message;
    if (commandArray.length == 2) {
        String category = commandArray[1];
        message = categoryService.addRoot(category, chatId);
    } else if (commandArray.length == 3) {
        String child = commandArray[1];
        String parent = commandArray[2];
        message = categoryService.add(child, parent, chatId);
    } else {
        message = "Вы неверно оформили команду /addElement";
    }

    SendMessage response = new SendMessage();
    // Настройка и отправка ответного сообщения через  Telegram API клиентскую реализацию
    // client.sendTelegramMessage(response);

}



    public String getCommand() {
        return "/addElement";
    }



}


