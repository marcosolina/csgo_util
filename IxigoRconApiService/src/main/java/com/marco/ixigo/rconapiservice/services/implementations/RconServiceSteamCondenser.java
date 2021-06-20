package com.marco.ixigo.rconapiservice.services.implementations;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.GameServer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;
import com.marco.ixigo.rconapiservice.services.interfaces.RconService;
import com.marco.utils.MarcoException;

public class RconServiceSteamCondenser implements RconService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RconServiceSteamCondenser.class);
    @Autowired
    private MessageSource msgSource;

    @Override
    public String sendRconCommand(String host, int port, String rconPassw, String rconCmd) throws MarcoException {

        if (host == null || host.isEmpty()) {
            throw new MarcoException(msgSource.getMessage("CSGOAPI00001", null, LocaleContextHolder.getLocale()));
        }
        if (port == 0) {
            throw new MarcoException(msgSource.getMessage("CSGOAPI00002", null, LocaleContextHolder.getLocale()));
        }
        if (rconPassw == null || rconPassw.isEmpty()) {
            throw new MarcoException(msgSource.getMessage("CSGOAPI00003", null, LocaleContextHolder.getLocale()));
        }
        if (rconCmd == null || rconCmd.isEmpty()) {
            throw new MarcoException(msgSource.getMessage("CSGOAPI00004", null, LocaleContextHolder.getLocale()));
        }

        try {
            GameServer server = new SourceServer(host, port);
            LOGGER.debug("Authenticating on the server");
            server.rconAuth(rconPassw);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Sending RCON command: %s", rconCmd));
            }
            String response = server.rconExec(rconCmd);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("RCON response: %s", response));
            }
            return response;
        } catch (SteamCondenserException | TimeoutException e) {
            if (LOGGER.isTraceEnabled()) {
                e.printStackTrace();
            }
            throw new MarcoException(msgSource.getMessage("CSGOAPI00005", null, LocaleContextHolder.getLocale()));
        }
    }

}
