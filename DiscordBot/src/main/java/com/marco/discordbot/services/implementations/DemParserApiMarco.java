package com.marco.discordbot.services.implementations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;

import com.marco.discordbot.config.properties.DemParserProperties;
import com.marco.discordbot.enums.BotConfigKey;
import com.marco.discordbot.enums.TeamType;
import com.marco.discordbot.model.entities.EntityBotConfig;
import com.marco.discordbot.model.rest.roundparser.ActivePlayersResponse;
import com.marco.discordbot.model.rest.roundparser.Teams;
import com.marco.discordbot.model.rest.roundparser.User;
import com.marco.discordbot.repositories.interfaces.RepoEntityBotConfig;
import com.marco.discordbot.services.interfaces.DemParserApi;
import com.marco.utils.network.MarcoNetworkUtils;

public class DemParserApiMarco implements DemParserApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemParserApiMarco.class);

    @Autowired
    private DemParserProperties demProps;
    @Autowired
    private MarcoNetworkUtils mnu;
    @Autowired
    private RepoEntityBotConfig repoBotConfig;

    @Override
    public Map<TeamType, List<User>> getCurrentActivePlayersOnTheIxiGoServer() {
        try {
            URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getGetActivePlayers());
            ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.empty());
            if (clientResp.statusCode() == HttpStatus.OK) {
                return mnu.getBodyFromResponse(clientResp, ActivePlayersResponse.class).getPlayers();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Teams generateCsgoTeams() {
        Map<TeamType, List<User>> inGamePlayers = getCurrentActivePlayersOnTheIxiGoServer();

        if (inGamePlayers != null && inGamePlayers.size() == 2) {
            StringBuilder sb = new StringBuilder();
            inGamePlayers.get(TeamType.CT).stream().forEach(u -> sb.append("," + u.getSteamId()));
            inGamePlayers.get(TeamType.TERRORISTS).stream().forEach(u -> sb.append("," + u.getSteamId()));
            Map<String, String> queryParam = new HashMap<>();
            if (sb.length() == 0) {
                return null;
            }
            queryParam.put("usersIDs", sb.substring(1));
            try {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Calling the API to balance the teams");
                }
                /*
                 * Call the Round Parser service to balance the teams
                 */
                URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getCreateTeams(getConfigRoundToConsiderForSplitTeam()));
                ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
                if (clientResp.statusCode() == HttpStatus.OK) {
                    return mnu.getBodyFromResponse(clientResp, Teams.class);
                } else {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error(String.format("Error when calling: %s", url.toString()));
                    }
                }

            } catch (MalformedURLException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private int getConfigRoundToConsiderForSplitTeam() {
        EntityBotConfig config = repoBotConfig.fingConfig(BotConfigKey.ROUNDS_TO_CONSIDER_FOR_TEAM_CREATION);
        int roundsToConsider = 50;
        if (config != null) {
            try {
                roundsToConsider = Integer.parseInt(config.getConfigVal());
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
        return roundsToConsider;
    }

    @Override
    public boolean balanceTheTeams() {
        LOGGER.debug("DemParserApiMarco.balanceTheTeams");

        Teams teams = generateCsgoTeams();
        if (teams != null) {

            /*
             * Sending the list of terrorist players
             */
            StringBuilder sb = new StringBuilder();
            teams.getTeams().get(0).getMembers().stream().forEach(m -> sb.append("," + m.getSteamID()));
            Map<String, String> queryParam = new HashMap<>();
            queryParam.put("terroristIDs", sb.substring(1));

            try {
                URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getMovePlayers());
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Setting terrorists: %s", queryParam.toString()));
                }

                ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.of(queryParam));
                boolean ok = clientResp.statusCode() == HttpStatus.OK;
                if (ok) {
                    LOGGER.debug("CSGO Teams set");
                } else {
                    LOGGER.debug("CSGO Teams NOT set");
                }
                return ok;
            } catch (MalformedURLException e) {
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public boolean restartIxiGoRound() {
        try {
            URL url = new URL(demProps.getProtocol(), demProps.getHost(), demProps.getRestartRound());
            ClientResponse clientResp = mnu.performGetRequest(url, Optional.empty(), Optional.empty());
            boolean ok = clientResp.statusCode() == HttpStatus.OK;
            if (ok) {
                LOGGER.debug("Round restarted");
            } else {
                LOGGER.debug("Failed to restart the round");
            }
            return ok;
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
