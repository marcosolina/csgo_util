package com.ixigo.demmanager.services.interfaces;

import java.io.File;

import reactor.core.publisher.Mono;

public interface DemFileFormatChecker {

	/**
	 * Returns true if it is a Counter-Strike 2 dem file, false otherwise
	 * @param file
	 * @return
	 */
	public Mono<Boolean> isCs2DemFile(File file);
}
