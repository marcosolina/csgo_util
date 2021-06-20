package com.marco.ixigo.discordbot.model.discord;

import com.marco.utils.http.MarcoResponse;

public class GetConfigValue extends MarcoResponse {
    private BotConfig config;

    public BotConfig getConfig() {
        return config;
    }

    public void setConfig(BotConfig config) {
        this.config = config;
    }

}
