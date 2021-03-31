package com.marco.csgoutil.roundparser.repositories.interfaces;

import java.util.List;

import com.marco.csgoutil.roundparser.model.entities.EntityUser;

public interface RepoUser {
	public List<EntityUser> getUsers();
	public void insertUpdateUser(EntityUser user);
	public EntityUser findById(String steamID);
}
