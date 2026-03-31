package org.service;

import org.repository.IMessageRepository;
import org.repository.IUserRepository;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
public class RenderService {
    private final IMessageRepository messageRepository;
    private final IUserRepository userRepository;

    public RenderService(IMessageRepository messageRepository, IUserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 14, timeUnit = TimeUnit.MINUTES)
    public void pingRender() throws IOException {
        String renderUrl = "https://message-board-nnel.onrender.com";

        HttpURLConnection uiRequest = (HttpURLConnection) new URL(renderUrl + "/register").openConnection();
        uiRequest.setRequestMethod("POST");
        uiRequest.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        uiRequest.getResponseCode();
        uiRequest.disconnect();

        HttpURLConnection swaggerRequest = (HttpURLConnection) new URL(renderUrl + "/swagger-ui/index.html").openConnection();
        swaggerRequest.getResponseCode();
        swaggerRequest.disconnect();
    }

    @Scheduled(cron = "0 56 3 * * *", zone = "Europe/Berlin")
    public void wipeData() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
    }
}
