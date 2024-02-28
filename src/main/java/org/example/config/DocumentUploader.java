package org.example.config;

import org.example.command.Command;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс DocumentUploader используется для загрузки документов на сервер Telegram.
 */
@Component
public class DocumentUploader implements Command {
    public String uploadDocument(String chatId) {
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());

        String url = "https://api.telegram.org/bot<6875863405:AAHAHq1uUMJHrdaWuE6afbUMJKqiFuMqjHw>/sendDocument";
        MultiValueMap<String, Object> requestParameters = new LinkedMultiValueMap<>();
        requestParameters.add("chat_id", chatId);

        File documentFile = new File();
        FileSystemResource documentResource = new FileSystemResource(String.valueOf(documentFile));
        requestParameters.add("document", documentResource);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(requestParameters, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        System.out.println("Document uploaded successfully!");
        return url;
    }

    @Override
    public void execute(Update update) throws TelegramApiException {

    }

    @Override
    public String getCommand() {
        return "/upload";
    }

    @Override
    public void execute() {

    }
}