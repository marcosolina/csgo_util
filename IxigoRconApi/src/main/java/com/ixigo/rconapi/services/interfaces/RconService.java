package com.ixigo.rconapi.services.interfaces;

import com.ixigo.library.errors.IxigoException;

import reactor.core.publisher.Mono;

/**
 * Interface that defines the RCON operations
 * 
 * @author Marco
 *
 */
public interface RconService {

    /**
     * It sends the RCON command to the requested server
     * 
     * @param host
     * @param port
     * @param rconPassw
     * @param rconCmd
     * @return The CSGO RCON Server response
     * @throws MarcoException
     */
    public Mono<String> sendRconCommand(String host, int port, String rconPassw, String rconCmd) throws IxigoException;
}
