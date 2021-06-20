package com.marco.ixigo.discordbot.model.discord;

import com.marco.ixigo.discordbot.enums.BotConfigKey;

public class BotConfig {
    private BotConfigKey configKey;
    private String configVal;

    public BotConfig() {
        
    }
    
    public BotConfig(BotConfigKey configKey, String configVal) {
        this.configKey = configKey;
        this.configVal = configVal;
    }
    
    public BotConfigKey getConfigKey() {
        return configKey;
    }

    public void setConfigKey(BotConfigKey configKey) {
        this.configKey = configKey;
    }

    public String getConfigVal() {
        return configVal;
    }

    public void setConfigVal(String configVal) {
        this.configVal = configVal;
    }

}
