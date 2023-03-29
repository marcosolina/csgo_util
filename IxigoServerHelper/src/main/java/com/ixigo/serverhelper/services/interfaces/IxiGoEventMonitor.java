package com.ixigo.serverhelper.services.interfaces;

/**
 * Monitor for the IxiGo Game Server events
 * 
 * @author Marco
 *
 */
public interface IxiGoEventMonitor {
    /**
     * It checks if a new IxiGo event occurred on the game server and notify the
     * RCON service
     * 
     * @return
     */
    public void scheduledCheckForNewEvent();
}
