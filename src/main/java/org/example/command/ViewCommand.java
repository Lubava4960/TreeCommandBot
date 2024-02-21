package org.example.command;

import lombok.Data;
import org.example.config.TelegramBotListener;
import org.example.service.CategoryService;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс  для отображения дерева категорий при вызове команды
 * /viewTree.
 */
@Data
@Service
public class ViewCommand implements Command{
    private final TelegramBotListener telegramBotListener;
    private final CategoryService categoryService;


    /**
     * Метод для выполнения команды /viewTree. Отправляет сообщение, где показано дерево категорий
     *
     * @param update объект {@link Update}, содержит информацию о сообщении пользователя.
     */
    @Override
     public void execute(org.telegram.telegrambots.meta.api.objects.Update update) throws TelegramApiException {
         long chatId=update.getMessage().getChatId();
        String message = categoryService.view(chatId);
        telegramBotListener.sendMessage(chatId, message);
      }

    @Override
    public void execute(Update update) throws TelegramApiException {

    }
    @Override
    public String getCommand() {
        return "/viewCommand";
    }

}

