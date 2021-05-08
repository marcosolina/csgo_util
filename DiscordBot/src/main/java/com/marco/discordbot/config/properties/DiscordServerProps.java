package com.marco.discordbot.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "discordbot.discord")
public class DiscordServerProps {
    private Long serverId;
    private VoiceChannels voiceChannels;

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
}
