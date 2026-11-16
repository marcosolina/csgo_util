package com.ixigo.serverhelper.services.interfaces;

import com.ixigo.serverhelper.models.svc.SvcCsgoConfig;

import reactor.core.publisher.Mono;

public interface CsgoServerConfigManager {
	public Mono<SvcCsgoConfig> getServerConfig();
}
