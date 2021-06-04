package com.marco.discordbot.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.marco.discordbot.enums.BotConfigKey;

@Entity
@Table(name = "BOT_CONFIG")
public class EntityBotConfig {
    
    @Id
    @Column(name = "CONFIG_KEY")
    @Enumerated(EnumType.STRING)
    private BotConfigKey configKey;
    @Column(name = "CONFIG_VAL")
    private String configVal;

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
