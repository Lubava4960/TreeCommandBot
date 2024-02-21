package org.example.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.example.config.TelegramBotListener;
import org.example.service.CategoryService;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс для удаления командой категорий при вызове команды
 * /removeElement.

 */

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RemoveCommand implements Command {
    private final TelegramBotListener telegramBotListener;
    private final CategoryService categoryService;
    private org.telegram.telegrambots.meta.api.objects.Update update;


    @Override
    public void execute(Update update) throws TelegramApiException {

    }

    /**
     * Метод для выполнения команды /removeElement.
     * Отправляет сообщение об удалении элемента или об ошибке, если пользователь неверно удалил команду
     *
     * @param update объект {@link Update}, содержит информацию о сообщении пользователя.
     */
    // @Override
    public void execute(org.telegram.telegrambots.meta.api.objects.Update update) throws TelegramApiException {
          String message;
          long chatId = update.getMessage().getChatId();
           String text = update.getMessage().getText().trim();
           String[] commandArray = text.replaceAll("\\s+", " ").split("\\s+<");
           if (commandArray.length == 2) {
              String category = StringUtils.substring(commandArray[1], 0, commandArray[1].length() - 1);
              message = categoryService.remove(category, chatId);
           } else {
              message = "Вы неверно оформили команду /removeElement";
          }
          telegramBotListener.sendMessage(chatId,text);
         }

    @Override
    public String getCommand() {
        return "/removeCommand";
    }


}


