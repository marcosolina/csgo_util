package com.marco.ixigo.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.marco.ixigo.discordbot.services.discord")
public class DiscordProps {
    private Long serverId;
    private VoiceChannels voiceChannels;
    private TextChannels textChannels;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public VoiceChannels getVoiceChannels() {
        return voiceChannels;
    }

    public void setVoiceChannels(VoiceChannels voiceChannels) {
        this.voiceChannels = voiceChannels;
    }

    public TextChannels getTextChannels() {
        return textChannels;
    }

    public void setTextChannels(TextChannels textChannels) {
        this.textChannels = textChannels;
    }
}
