package com.marco.discordbot.model.rest.roundparser;

import java.util.ArrayList;
import java.util.List;

import com.marco.discordbot.model.rest.DiscordUser;

public class GeneratedTeams {
    private List<DiscordUser> terrorist = new ArrayList<>();
    private List<DiscordUser> ct = new ArrayList<>();

    public List<DiscordUser> getTerrorist() {
        return terrorist;
    }

    public void setTerrorist(List<DiscordUser> terrorist) {
        this.terrorist = terrorist;
    }

    public List<DiscordUser> getCt() {
        return ct;
    }

    public void setCt(List<DiscordUser> ct) {
        this.ct = ct;
    }

}
