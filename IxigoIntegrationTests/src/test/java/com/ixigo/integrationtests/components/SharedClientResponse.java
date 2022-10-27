package com.ixigo.integrationtests.components;

import org.springframework.web.reactive.function.client.ClientResponse;

import reactor.core.publisher.Mono;

public class SharedClientResponse {
	private ClientResponse sharedResp;

	public ClientResponse getSharedResp() {
		return sharedResp;
	}

	public void setSharedResp(Mono<ClientResponse> sharedResp) {
		this.sharedResp = sharedResp.block();
	}
	
	
}
