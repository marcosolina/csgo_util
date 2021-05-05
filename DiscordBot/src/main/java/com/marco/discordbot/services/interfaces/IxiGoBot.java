package com.marco.discordbot.services.interfaces;

import java.util.List;

import com.marco.discordbot.model.rest.DiscordUser;
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
    public List<DiscordUser> getMembers() throws MarcoException;

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
    public boolean balanceTheTeamsAndMoveThemInTheAppropirateChannel() throws MarcoException;
}
