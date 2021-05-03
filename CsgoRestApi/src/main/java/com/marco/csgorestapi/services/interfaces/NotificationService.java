package com.marco.csgorestapi.services.interfaces;

/**
 * This interface provides some methods to send notifications
 * 
 * @author Marco
 *
 */
public interface NotificationService {

	/**
	 * It will notify that an error occur while dispatching an event
	 * 
	 * @param recipients
	 */
	public void sendEventServiceError(String title, String message);

}
