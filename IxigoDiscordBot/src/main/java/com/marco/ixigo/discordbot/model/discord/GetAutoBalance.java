package com.marco.ixigo.discordbot.model.discord;

import com.marco.utils.http.MarcoResponse;

public class GetAutoBalance extends MarcoResponse {
    private boolean autoBalanceActive;

    public boolean isAutoBalanceActive() {
        return autoBalanceActive;
    }

    public void setAutoBalanceActive(boolean autoBalanceActive) {
        this.autoBalanceActive = autoBalanceActive;
    }
}
