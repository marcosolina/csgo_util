package com.marco.discordbot.repositories.interfaces;

import com.marco.discordbot.enums.BotConfigKey;
import com.marco.discordbot.model.entities.EntityBotConfig;

public interface RepoEntityBotConfig {
    /**
     * It returns the entity associated to the provided key
     * 
     * @param key
     * @return
     */
    public EntityBotConfig fingConfig(BotConfigKey key);

    /**
     * Inert or replace the entity
     * 
     * @param config
     * @return
     */
    public boolean saveConfig(EntityBotConfig config);
}
