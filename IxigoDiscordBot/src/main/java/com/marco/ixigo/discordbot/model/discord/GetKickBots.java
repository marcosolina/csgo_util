package com.marco.ixigo.discordbot.model.discord;

import com.marco.utils.http.MarcoResponse;

public class GetKickBots extends MarcoResponse {
    private boolean kickBotsActive;

    public boolean isKickBotsActive() {
        return kickBotsActive;
    }

    public void setKickBotsActive(boolean kickBotsActive) {
        this.kickBotsActive = kickBotsActive;
    }

}
