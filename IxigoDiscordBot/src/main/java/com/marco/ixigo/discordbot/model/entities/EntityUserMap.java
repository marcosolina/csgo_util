package com.marco.ixigo.discordbot.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS_MAP")
public class EntityUserMap {
    @Id
    @Column(name = "DISCORD_ID")
    private Long discordId;
    @Column(name = "STEAM_ID")
    private String steamId;
    @Column(name = "DISCORD_NAME")
    private String discordName;
    @Column(name = "STEAM_NAME")
    private String steamName;

    public Long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(Long discordId) {
        this.discordId = discordId;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getDiscordName() {
        return discordName;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public String getSteamName() {
        return steamName;
    }

    public void setSteamName(String steamName) {
        this.steamName = steamName;
    }
}
