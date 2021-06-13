package com.marco.ixigo.demmanager.services.interfaces;

/**
 * This interface provides some methods to send notifications
 * 
 * @author Marco
 *
 */
public interface NotificationService {

    /**
     * It will notify the recipients that the parsing process is complete
     * 
     * @param recipients
     */
    public void sendParsingCompleteNotification(String title, String message);

}
