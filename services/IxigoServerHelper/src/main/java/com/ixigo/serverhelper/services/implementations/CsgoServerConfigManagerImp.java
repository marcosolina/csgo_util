package com.ixigo.serverhelper.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;

import com.ixigo.serverhelper.config.properties.CsgoServerProps;
import com.ixigo.serverhelper.models.svc.SvcCsgoConfig;
import com.ixigo.serverhelper.services.interfaces.CsgoServerConfigManager;

import reactor.core.publisher.Mono;

public class CsgoServerConfigManagerImp implements CsgoServerConfigManager{
	@Autowired
	private CsgoServerProps serverProps;
	
	@Override
	public Mono<SvcCsgoConfig> getServerConfig() {
		var config = new SvcCsgoConfig();
		config.setHostName(serverProps.getHostName());
		config.setPort(serverProps.getPort());
		return Mono.just(config);
	}

}
