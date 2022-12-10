package com.ixigo.discordbot.commands.config;

import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;
import com.ixigo.models.rest.RestBotConfig;

public class PutBotConfigCmd implements WebCommandRequest<Void> {
	private RestBotConfig config;

	public PutBotConfigCmd(RestBotConfig config) {
		super();
		this.config = config;
	}

	public RestBotConfig getConfig() {
		return config;
	}

	public void setConfig(RestBotConfig config) {
		this.config = config;
	}

}
