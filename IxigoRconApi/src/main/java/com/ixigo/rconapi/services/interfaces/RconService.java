package com.ixigo.rconapi.services.interfaces;

import com.ixigo.library.errors.IxigoException;
import com.ixigo.rconapi.models.service.SvcRconRequest;

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
     * @param request
     * @return The CSGO RCON Server response
     * @throws MarcoException
     */
    public Mono<String> sendRconCommand(SvcRconRequest request) throws IxigoException;
}
