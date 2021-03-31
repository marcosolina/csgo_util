package com.marco.csgoutil.roundparser.repositories.interfaces;

import java.util.List;

import com.marco.csgoutil.roundparser.model.entities.EntityUser;

/**
 * CRUD Operation that you can perform on the User table
 * 
 * @author Marco
 *
 */
public interface RepoUser {
	/**
	 * It returns the full list of know Users
	 * 
	 * @return
	 */
	public List<EntityUser> getUsers();

	/**
	 * It insert/update the user definition
	 * 
	 * @param user
	 */
	public void insertUpdateUser(EntityUser user);

	/**
	 * It returns the User definition
	 * 
	 * @param steamID
	 * @return
	 */
	public EntityUser findById(String steamID);
}
