package org.example.command;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.example.config.TelegramBotListener;
import org.example.service.CategoryService;
import org.hibernate.sql.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс для добавления категорий при вызове команды /
 /addElement.
 */

@Data
public class AddCommand implements Command{
    private final TelegramBotListener telegramBotListener;
    private final CategoryService categoryService;
    private org.telegram.telegrambots.meta.api.objects.Update update;

    public AddCommand(TelegramBotListener telegramBotListener,CategoryService categoryService) {
        this.telegramBotListener = telegramBotListener;
        this.categoryService = categoryService;
    }


    @Override
    public void execute(org.telegram.telegrambots.meta.api.objects.Update update) throws TelegramApiException {

    }

    /**
     * Метод для выполнения команды /addCommand.
     * Отправляет сообщение о добавлении элемента или об ошибке, если пользователь неверно оформил команду
     *
     * @param update объект {@link Update}, содержит информацию о сообщении пользователя.
     */
    @Override
     public void execute(Update update) throws TelegramApiException {

        String message;
        long chatId = Long.parseLong(update.getTableName());
          String text = update.getTableName();
          String[] commandArray = text.replaceAll("\\s+", " ").split("\\s+<");
          if (commandArray.length == 2) {
            //если добавляем корневой элемент
             String category = StringUtils.substring(commandArray[1], 0, commandArray[1].length() - 1);
             message = categoryService.addRoot(category, chatId);
          } else if (commandArray.length == 3) {
            //если добавляем дочерний элемент к родительскому
             String child = StringUtils.substring(commandArray[1], 0, commandArray[1].length() - 1);
             String parent = StringUtils.substring(commandArray[2], 0, commandArray[2].length() - 1);
             message = categoryService.add(child, parent, chatId);
          } else {
             message = "Вы неверно оформили команду /addCommand";
          }

        telegramBotListener.sendMessage(chatId, message);
     }

    @Override
    public String getCommand() {
        return "/addRoot";
    }

}


