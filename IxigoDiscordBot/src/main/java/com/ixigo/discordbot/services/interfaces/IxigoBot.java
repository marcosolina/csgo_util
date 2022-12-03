package com.ixigo.discordbot.services.interfaces;

import com.ixigo.library.errors.IxigoException;

public interface IxigoBot {
	/**
     * But the bot online
     * 
     * @return
     * @throws MarcoException
     */
    public boolean start() throws IxigoException;
    
    /**
     * Disconnect the bot
     * 
     * @return
     * @throws MarcoException
     */
    public boolean stop() throws IxigoException;
}
