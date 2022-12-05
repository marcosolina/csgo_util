package com.ixigo.discordbot.repositories.interfaces;

import java.util.List;

import com.ixigo.discordbot.models.repo.Users_mapDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RepoUsersMap {
	public Mono<Boolean> insertOrUpdate(Users_mapDto entity);
	public Mono<Users_mapDto> findById(Long discordId);
	public Flux<Users_mapDto> getAll();
	public Flux<Users_mapDto> findAllById(List<Long> discordIds);
}
