package com.marco.ixigo.discordbot.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.marco.ixigo.discordbot.config.properties.PlayersManagerProps;
import com.marco.ixigo.discordbot.config.properties.RconApiProps;
import com.marco.ixigo.discordbot.enums.BotConfigKey;
import com.marco.ixigo.discordbot.enums.TeamType;
import com.marco.ixigo.discordbot.model.demmanager.User;
import com.marco.ixigo.discordbot.model.entities.EntityBotConfig;
import com.marco.ixigo.discordbot.model.playersmanager.Team;
import com.marco.ixigo.discordbot.model.playersmanager.Teams;
import com.marco.ixigo.discordbot.model.rconapi.RconHttpRequest;
import com.marco.ixigo.discordbot.model.rconapi.RconHttpResponse;
import com.marco.ixigo.discordbot.repositories.interfaces.RepoEntityBotConfig;
import com.marco.ixigo.discordbot.services.interfaces.IxiGoGameServer;
import com.marco.utils.MarcoException;
import com.marco.utils.network.MarcoNetworkUtils;

public class IxiGoGameServerMarco implements IxiGoGameServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(IxiGoGameServerMarco.class);
    @Autowired
    private MarcoNetworkUtils mnu;
    @Autowired
    private RconApiProps rconApiProps;
    @Autowired
    private PlayersManagerProps playersManProps;
    @Autowired
    private RepoEntityBotConfig repoConfig;
    @Autowired
    private MessageSource msgSource;

    @Override
    public Map<TeamType, List<User>> getCurrentActivePlayersOnTheIxiGoServer() throws MarcoException {
        try {
            /*
             * Call the RCON service
             */
            URL url = new URL(rconApiProps.getProtocol(), rconApiProps.getHost(), rconApiProps.getPort(),
                    rconApiProps.getSendcommand());
            RconHttpRequest request = getDefaultRconHttpRequest();
            request.setRconCmd("sm_list_players");

            ClientResponse resp = mnu.performPostRequest(url, Optional.empty(), Optional.of(request));
            if (resp.statusCode() != HttpStatus.OK) {
                throw new MarcoException("Not able to send the RCON command");
            }

            /**
             * Parse the RCON response
             */
            RconHttpResponse respBody = mnu.getBodyFromResponse(resp, RconHttpResponse.class);
            List<User> terrorists = new ArrayList<>();
            List<User> ct = new ArrayList<>();
            if (respBody.getRconResponse() != null && !respBody.getRconResponse().isEmpty()) {
                String[] lines = respBody.getRconResponse().split("\\n");
                if (lines.length > 2) {
                    boolean t = true;
                    for (int i = 0; i < lines.length; i++) {
                        if (lines[i].equals(TeamType.CT.getDesc())) {
                            t = false;
                            continue;
                        }
                        if (lines[i].equals(TeamType.TERRORISTS.getDesc())) {
                            t = true;
                            continue;
                        }

                        if (t) {
                            terrorists.add(new User(lines[i]));
                        } else {
                            ct.add(new User(lines[i]));
                        }

                    }
                }
            }

            Map<TeamType, List<User>> players = new EnumMap<>(TeamType.class);
            players.put(TeamType.TERRORISTS, terrorists);
            players.put(TeamType.CT, ct);
            return players;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new MarcoException(e);
        }
    }

    @Override
    public Teams generateCsgoTeams() throws MarcoException {
        Map<TeamType, List<User>> activePlayers = this.getCurrentActivePlayersOnTheIxiGoServer();
        List<String> players = new ArrayList<>();
        activePlayers.forEach((k, v) -> v.forEach(u -> players.add(u.getSteamId())));

        if (!players.isEmpty()) {
            try {
                
                StringBuilder sb = new StringBuilder();
                players.stream().forEach(s -> sb.append("," + s));
                
                
                URL url = new URL(playersManProps.getProtocol(), playersManProps.getHost(), playersManProps.getPort(), playersManProps.getGenerateTeams());
                Map<String, String> queryParameters = new HashMap<>();
                queryParameters.put("usersIDs", sb.toString().substring(1));
                
                EntityBotConfig config = repoConfig.fingConfig(BotConfigKey.ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION);
                if (config != null) {
                    queryParameters.put("gamesCounter", config.getConfigVal());
                }

                ClientResponse resp = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParameters));
                if (resp.statusCode() != HttpStatus.OK) {
                    throw new MarcoException(
                            msgSource.getMessage("DISCBOT00002", null, LocaleContextHolder.getLocale()));
                }
                return mnu.getBodyFromResponse(resp, Teams.class);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage());
                throw new MarcoException(e);
            }
        }
        throw new MarcoException(msgSource.getMessage("DISCBOT00003", null, LocaleContextHolder.getLocale()));
    }

    @Override
    public boolean movePlayersToAppropriateTeam(Teams teams) throws MarcoException {
        Team terrorists = teams.getTeams().get(0);

        StringBuilder sb = new StringBuilder();
        terrorists.getMembers().stream().forEach(m -> {
            sb.append(" \"");
            sb.append(m.getSteamID());
            sb.append("\"");
        });

        RconHttpRequest request = getDefaultRconHttpRequest();
        request.setRconCmd(String.format("sm_move_players %s dummy", sb.toString().substring(1)));
        try {
            URL url = new URL(rconApiProps.getProtocol(), rconApiProps.getHost(), rconApiProps.getPort(),
                    rconApiProps.getSendcommand());
            ClientResponse resp = mnu.performPostRequest(url, Optional.empty(), Optional.of(request));
            return resp.statusCode() != HttpStatus.OK;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new MarcoException(e);
        }
    }

    @Override
    public boolean restartIxiGoRound() throws MarcoException {
        RconHttpRequest request = getDefaultRconHttpRequest();
        request.setRconCmd("mp_restartgame 5");
        try {
            URL url = new URL(rconApiProps.getProtocol(), rconApiProps.getHost(), rconApiProps.getPort(),
                    rconApiProps.getSendcommand());
            ClientResponse resp = mnu.performPostRequest(url, Optional.empty(), Optional.of(request));
            return resp.statusCode() != HttpStatus.OK;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage());
            throw new MarcoException(e);
        }
    }

    private RconHttpRequest getDefaultRconHttpRequest() {
        RconHttpRequest request = new RconHttpRequest();
        request.setRconHost(rconApiProps.getCsgoServerHost());
        request.setRconPort(rconApiProps.getCsgoServerPort());
        request.setRconPass(rconApiProps.getCsgoServerPassw());
        return request;
    }

}
