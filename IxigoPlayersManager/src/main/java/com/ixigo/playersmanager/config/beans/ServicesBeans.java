package com.ixigo.playersmanager.config.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ixigo.playersmanager.services.implementation.PartitionTeamsIxigo;
import com.ixigo.playersmanager.services.implementation.PlayersManagerImp;
import com.ixigo.playersmanager.services.interfaces.PartitionTeams;
import com.ixigo.playersmanager.services.interfaces.PlayersManager;

@Configuration
public class ServicesBeans {
	@Bean
	public PlayersManager getPlayersManager() {
		return new PlayersManagerImp();
	}
	
	@Bean
	public PartitionTeams getPartitionTeamsIxigo() {
		return new PartitionTeamsIxigo();
	}
}
