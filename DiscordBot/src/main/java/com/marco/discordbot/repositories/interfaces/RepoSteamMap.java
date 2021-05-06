package com.marco.discordbot.repositories.interfaces;

import java.util.List;

import com.marco.discordbot.model.entities.EntitySteamMap;

public interface RepoSteamMap {
    public boolean persist(EntitySteamMap entity);

    public EntitySteamMap findById(Long discordId);

    public List<EntitySteamMap> getAll();
}
