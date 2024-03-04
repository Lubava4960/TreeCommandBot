package org.example.config;


import org.example.command.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * класс DocumentDownloader содержит метод downloadDocument, который принимает URL документа и путь, где
 * нужно сохранить загруженный документ. В методе создается объект URL для переданного URL документа,
 * открывается поток InputStream для чтения содержимого документа и поток FileOutputStream для записи данных в указанное место
 */
@Component
public class DocumentDownloader implements Command {
    private static final String BASE_URL = "https://api.telegram.org/bot<6875863405:AAHAHq1uUMJHrdaWuE6afbUMJKqiFuMqjHw>/getFile?file_id=";

    public void downloadDocument(String documentFileId, String saveLocation) {
        try {
            // Формируем URL для получения информации о файле
            URL url = new URL(BASE_URL + documentFileId);
            HttpURLConnection HttpURLConnection = (java.net.HttpURLConnection) url.openConnection();
            java.net.HttpURLConnection connection = null;
            connection.setRequestMethod("GET");

            // Получаем информацию о файле
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String json = reader.readLine();
            reader.close();

            // Получаем путь к файлу из информации о файле
            String filePath = getFilePathFromJson(json);
            if (filePath != null) {
                // Формируем URL для загрузки файла
                URL fileUrl = new URL("https://api.telegram.org/file/bot<6875863405:AAHAHq1uUMJHrdaWuE6afbUMJKqiFuMqjHw>/" + filePath);

                // Открываем поток для чтения и записи файла
                InputStream inputStream = fileUrl.openStream();
                FileOutputStream fileOutputStream = new FileOutputStream(saveLocation);

                // Читаем и записываем данные файла
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }

                // Закрываем потоки
                fileOutputStream.close();
                inputStream.close();

                System.out.println("Document downloaded successfully!");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilePathFromJson(String json) {
        // Получает путь к файлу из JSON-ответа
        // Обработка json и извлечение пути к файлу


        return "path/to/file.jpg";
    }


    @Override
    public void execute(Update update) throws TelegramApiException {


    }

    @Override
    public String getCommand() {
        return "/download";
    }




}
