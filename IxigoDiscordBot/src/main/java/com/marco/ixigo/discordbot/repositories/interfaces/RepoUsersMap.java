package com.marco.ixigo.discordbot.repositories.interfaces;

import java.util.List;

import com.marco.ixigo.discordbot.model.entities.EntityUserMap;

public interface RepoUsersMap {
    public boolean persist(EntityUserMap entity);

    public EntityUserMap findById(Long discordId);

    public List<EntityUserMap> getAll();
}
