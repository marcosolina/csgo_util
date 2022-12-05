package com.ixigo.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ixigo.discord")
public class DiscordProps {
	private String bottoken;
	private Long serverId;

	@NestedConfigurationProperty
	private DiscordTextChannelsProps textChannels;
	@NestedConfigurationProperty
	private DiscordVoiceChannelsProps voiceChannels;

	public String getBottoken() {
		return bottoken;
	}

	public void setBottoken(String bottoken) {
		this.bottoken = bottoken;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public DiscordTextChannelsProps getTextChannels() {
		return textChannels;
	}

	public void setTextChannels(DiscordTextChannelsProps textChannels) {
		this.textChannels = textChannels;
	}

	public DiscordVoiceChannelsProps getVoiceChannels() {
		return voiceChannels;
	}

	public void setVoiceChannels(DiscordVoiceChannelsProps voiceChannels) {
		this.voiceChannels = voiceChannels;
	}

}
