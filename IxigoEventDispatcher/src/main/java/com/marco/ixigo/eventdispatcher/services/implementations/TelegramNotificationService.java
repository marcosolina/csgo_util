package com.marco.ixigo.eventdispatcher.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import com.marco.ixigo.eventdispatcher.services.interfaces.NotificationService;
import com.marco.utils.network.MarcoNetworkUtils;

public class TelegramNotificationService implements NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramNotificationService.class);
    
    @Value("${com.marco.ixigo.eventdispatcher.notification.telegram.enabled}")
    private boolean notificationEnabled;
    @Value("${com.marco.ixigo.eventdispatcher.notification.telegram.telegramToken}")
    private String botToken;
    @Value("${com.marco.ixigo.eventdispatcher.notification.telegram.chatGroupId}")
    private String chatId;
    
    @Autowired
    @Qualifier("NetworkUtilsNotBalanced")
    private MarcoNetworkUtils mnu;
    
    @Override
    public void sendEventServiceError(String title, String message) {
        if(!notificationEnabled) {
            return;
        }
        
        LOGGER.info("Sending Telegram notification");
        try {
            URL url = new URL(String.format("https://api.telegram.org/bot%s/sendMessage", botToken));
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("chat_id", chatId);
            queryParam.put("text", String.format("%s%n%n%s", title, message));
            mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
            LOGGER.info("Telegram notification sent");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
    }

}
