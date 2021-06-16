package com.marco.ixigo.discordbot.repositories.implementations;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.marco.ixigo.discordbot.enums.BotConfigKey;
import com.marco.ixigo.discordbot.model.entities.EntityBotConfig;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoEntityBotConfig;

@Transactional
public class RepoEntityBotConfigPostgres implements RepoEntityBotConfig {
    @PersistenceContext
    private EntityManager em;

    @Override
    public EntityBotConfig fingConfig(BotConfigKey key) {
        return em.find(EntityBotConfig.class, key);
    }

    @Override
    public boolean saveConfig(EntityBotConfig config) {
        em.merge(config);
        return true;
    }

}
