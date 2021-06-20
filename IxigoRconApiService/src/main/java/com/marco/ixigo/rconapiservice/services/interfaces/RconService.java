package com.marco.ixigo.rconapiservice.services.interfaces;

import com.marco.utils.MarcoException;

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
    public String sendRconCommand(String host, int port, String rconPassw, String rconCmd) throws MarcoException;
}
