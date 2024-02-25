package org.example.command;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.config.TelegramBotListener;
import org.example.service.CategoryService;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс  для отображения дерева категорий при вызове команды
 * /viewTree.
 */
@EqualsAndHashCode()
@Slf4j
@Data
@Generated
@Setter
@Component

public class ViewCommand implements Command{
    private  TelegramBotListener telegramBotListener;
    private  CategoryService categoryService;


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
    public String getCommand() {
        return "/viewElement";
    }

    @Override
    public void execute() {

    }


}

