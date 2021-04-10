package com.marco.csgoutil.roundparser.services.interfaces;

import java.util.List;

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
	public void sendParsingCompleteNotification(List<String> recipients, String title, String message);

}
