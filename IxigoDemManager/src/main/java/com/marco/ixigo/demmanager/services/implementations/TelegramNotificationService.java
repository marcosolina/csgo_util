package com.marco.ixigo.demmanager.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.marco.ixigo.demmanager.config.properties.TelegramProperties;
import com.marco.ixigo.demmanager.services.interfaces.NotificationService;
import com.marco.utils.network.MarcoNetworkUtils;

public class TelegramNotificationService implements NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramNotificationService.class);

    @Autowired
    private TelegramProperties tp;
    @Autowired
    @Qualifier("NetworkUtilsNotBalanced")
    private MarcoNetworkUtils mnu;

    @Override
    public void sendParsingCompleteNotification(String title, String message) {
        if (!tp.isEnabled()) {
            return;
        }

        LOGGER.info("Sending Telegram notification");
        try {
            URL url = new URL(String.format("https://api.telegram.org/bot%s/sendMessage", tp.getTelegramToken()));
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("chat_id", tp.getChatGroupId());
            queryParam.put("text", String.format("%s%n%n%s", title, message));
            mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
        }
        LOGGER.info("Telegram notification sent");
    }
}
