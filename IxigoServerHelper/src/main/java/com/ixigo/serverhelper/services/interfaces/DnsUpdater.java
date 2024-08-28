package com.ixigo.serverhelper.services.interfaces;

import reactor.core.publisher.Mono;

public interface DnsUpdater {
	public Mono<Boolean> updateDnsEntry();
}
