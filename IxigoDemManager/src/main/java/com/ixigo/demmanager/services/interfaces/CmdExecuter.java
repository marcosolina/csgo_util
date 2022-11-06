package com.ixigo.demmanager.services.interfaces;

import java.util.List;

import com.ixigo.demmanager.models.svc.demdata.SvcUserGotvScore;
import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Flux;

public interface CmdExecuter {
	public Flux<SvcUserGotvScore> extractPlayersScore(List<String> cmd) throws IxigoException;
}
