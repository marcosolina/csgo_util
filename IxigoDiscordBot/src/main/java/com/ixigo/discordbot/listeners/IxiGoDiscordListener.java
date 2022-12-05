package com.ixigo.discordbot.listeners;

import com.ixigo.discordbot.services.interfaces.IxigoBot;

import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class IxiGoDiscordListener extends ListenerAdapter{
	private IxigoBot ixiGoBot;

    public IxiGoDiscordListener(IxigoBot ixiGoBot) {
        this.ixiGoBot = ixiGoBot;
    }
}
