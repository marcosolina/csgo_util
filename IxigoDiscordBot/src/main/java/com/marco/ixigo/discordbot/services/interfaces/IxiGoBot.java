package com.marco.ixigo.discordbot.services.interfaces;

import java.util.List;

import com.marco.ixigo.discordbot.enums.BotConfigKey;
import com.marco.ixigo.discordbot.model.Player;
import com.marco.ixigo.discordbot.model.discord.BotConfig;
import com.marco.ixigo.discordbot.model.discord.DiscordUser;
import com.marco.utils.MarcoException;

/**
 * It defines what you can do with the bot
 * 
 * @author Marco
 *
 */
public interface IxiGoBot {
    /**
     * But the bot online
     * 
     * @return
     * @throws MarcoException
     */
    public boolean start() throws MarcoException;

    /**
     * Disconnect the bot
     * 
     * @return
     * @throws MarcoException
     */
    public boolean stop() throws MarcoException;

    /**
     * Return the list of available users in the Discord server
     * 
     * @return
     */
    public List<DiscordUser> getAllDiscordServerMembers() throws MarcoException;

    /**
     * It moves all the members from the specific CSGO channel into the general
     * voice channel
     * 
     * @return
     * @throws MarcoException
     */
    public boolean moveAllMembersIntoGeneralChannel() throws MarcoException;

    /**
     * It will call the Round parser Service to generate the balanced teams and then
     * move members in the right voice channel
     * 
     * @return
     * @throws MarcoException
     */
    public boolean moveDiscordUsersInTheAppropirateChannel() throws MarcoException;

    /**
     * It retrieves the discords players which are in a chat, and then call the Dem
     * Service to generate the CSGO teams and move the players in the appropriate
     * team
     * 
     * @return
     * @throws MarcoException
     */
    public boolean balanceTheTeams() throws MarcoException;

    /**
     * It will restart the IxiGo Round
     * 
     * @return
     * @throws MarcoException
     */
    public boolean restartIxiGoRound() throws MarcoException;

    /**
     * It does not move any players, it just attempt a call to warm up the service
     * call
     * 
     * @return
     * @throws MarcoException
     */
    public boolean warmUpBalanceTeamApi() throws MarcoException;

    /**
     * It enables / disable the auto balance
     * 
     * @param active
     */
    public void setAutoBalance(boolean active);

    /**
     * It tells you if the option is acitve
     * 
     * @return
     */
    public boolean isAutobalance();

    /**
     * It returns the list of discord users mapped to the Steam user
     * 
     * @return
     * @throws MarcoException
     */
    public List<Player> getListOfMappedPlayers() throws MarcoException;

    /**
     * It stores the players details
     * 
     * @param player
     * @return
     * @throws MarcoException
     */
    public boolean storePlayersDetails(List<Player> players) throws MarcoException;

    /**
     * It update the config value
     * 
     * @param config
     * @return
     */
    public boolean updateBotConfig(BotConfig config) throws MarcoException;
    
    /**
     * Get the config value
     * @param key
     * @return
     * @throws MarcoException
     */
    public BotConfig getBotConfig(BotConfigKey key) throws MarcoException;
}