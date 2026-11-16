package com.ixigo.serverhelper.services.interfaces;

import reactor.core.publisher.Mono;

/**
 * It manages IO with the CS2 windows
 */
public interface Cs2SendInputs {
	/**
	 * It sends a string into the CS2 window input
	 * @param input
	 * @return
	 */
	public Mono<Boolean> sendInput(String input);

}
