package com.marco.ixigo.discordbot.repositories.interfaces;

import com.marco.ixigo.discordbot.enums.BotConfigKey;
import com.marco.ixigo.discordbot.model.entities.EntityBotConfig;

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
