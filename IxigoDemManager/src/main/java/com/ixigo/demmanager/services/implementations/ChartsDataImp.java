package com.ixigo.demmanager.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.demmanager.models.database.DtoMapPlayedCounter;
import com.ixigo.demmanager.models.svc.demdata.SvcMapPlayedCounter;
import com.ixigo.demmanager.repositories.interfaces.RepoUserScore;
import com.ixigo.demmanager.services.interfaces.ChartsData;

import reactor.core.publisher.Flux;
/**
 * My implementation of the @ChartsData
 * @author marco
 *
 */
public class ChartsDataImp implements ChartsData{
	private static final Logger _LOGGER = LoggerFactory.getLogger(ChartsDataImp.class);
	
	@Autowired
	private RepoUserScore repoUserScore;
	
	@Override
	public Flux<SvcMapPlayedCounter> countGamesOnAMap() {
		_LOGGER.trace("Inside: ChartsDataImp.countGamesOnAMap");
		Flux<DtoMapPlayedCounter> maps = repoUserScore.getMapsPlayed();
		return maps.map(dto -> {
			SvcMapPlayedCounter mp = new SvcMapPlayedCounter();
			mp.setCount(dto.getCount());
			mp.setMapName(dto.getMapName());
			return mp;
		});
	}
}
