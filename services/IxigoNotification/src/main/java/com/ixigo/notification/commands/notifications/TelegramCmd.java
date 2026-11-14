package com.ixigo.notification.commands.notifications;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.notification.model.rest.telegram.RestTelegramMessage;

public class TelegramCmd implements WebCommandRequest<Void> {
	private RestTelegramMessage message;

	public TelegramCmd(RestTelegramMessage message) {
		this.message = message;
	}

	public RestTelegramMessage getMessageToSend() {
		return message;
	}

	public void setMessageToSend(RestTelegramMessage message) {
		this.message = message;
	}
}
