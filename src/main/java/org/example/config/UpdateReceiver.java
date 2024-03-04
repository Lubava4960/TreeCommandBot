package org.example.config;

import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 *  класс UpdateReceiver принимает объект Update в конструкторе и предоставляет методы для получения различных значений из объекта Update.
 *
 *  Метод getCommand возвращает команду, полученную из текста сообщения, если оно присутствует.
 *  Метод getUserId возвращает идентификатор пользователя, полученный из объекта Update.
 *  Методы getDocumentUrl и getDocumentCaption возвращают соответственно URL документа и его подпись, если они присутствуют в объекте Update.
 */

@Data

public class UpdateReceiver {
    private org.telegram.telegrambots.meta.api.objects.Update update;

    public UpdateReceiver(Update update) {
        this.update = update;
    }

    public String getCommand() {
        // Получение команды из объекта Update
        if (update.hasMessage() && update.getMessage().hasText())  {
            // выполнение кода внутри условия
        }


        return " ";
    }

    public Long getUserId() {
        // Получение идентификатора пользователя из объекта Update
        if (update.hasMessage() && update.getMessage().hasText()) {
            // выполнение кода внутри условия
        }

        return 0L;
    }

    public String getDocumentUrl() {
        // Получение URL документа из объекта Update
        if (update.hasMessage() && update.getMessage().getDocument() != null) {
            Document document = update.getMessage().getDocument();
            String fileId = document.getFileId();
            String botToken = "6875863405:AAHAHq1uUMJHrdaWuE6afbUMJKqiFuMqjHw";
            return "https://api.telegram.org/bot" + botToken + "/getFile?file_id=" + fileId;
        }
        return "";
    }


    public String getDocumentCaption() {
        // Получение подписи к документу из объекта Update
        if (update.hasMessage() && update.getMessage().getCaption() != null) {
            return update.getMessage().getCaption();
        }
        return "";
    }
}

