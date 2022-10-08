package com.ixigo.demmanager.repositories.interfaces;

import com.ixigo.demmanager.models.entities.UsersDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<UsersDto> getUsers();

    /**
     * It insert/update the user definition
     * 
     * @param user
     */
    public Mono<Boolean> insertUpdateUser(UsersDto user);

    /**
     * It returns the User definition
     * 
     * @param steamID
     * @return
     */
    public Mono<UsersDto> findById(String steamID);
}
