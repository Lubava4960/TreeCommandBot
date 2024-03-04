package org.example.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.service.CategoryService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс для удаления командой категорий при вызове команды
 * /removeTree.

 */

@EqualsAndHashCode()
@Slf4j
@Data
@Generated
@Setter
@Component
public class RemoveCategory implements Command {
    private TelegramBotListener telegramBotListener;
    private CategoryService categoryService;
    private org.telegram.telegrambots.meta.api.objects.Update update;


    /**
     * Метод для выполнения команды /removeElement.
     * Отправляет сообщение об удалении элемента или об ошибке, если пользователь неверно удалил команду
     */


   @Override
    public void execute(Update update) throws TelegramApiException {
          String message;
          long chatId = update.getMessage().getChatId();
           String text = update.getMessage().getText().trim();
           String[] commandArray = text.replaceAll("\\s+", " ").split("\\s+<");
           if (commandArray.length == 2) {
              String category = StringUtils.substring(commandArray[1], 0, commandArray[1].length() - 1);
              message = categoryService.remove(category, chatId);
           } else {
              message = "Вы неверно оформили команду /removeTree";
          }
          telegramBotListener.sendMessage(chatId,text);


   }

    @Override
    public String getCommand() {
        return "/removeTree";
    }




}


