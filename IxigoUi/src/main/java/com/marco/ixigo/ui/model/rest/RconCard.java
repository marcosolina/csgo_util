package com.marco.ixigo.ui.model.rest;

public abstract class RconCard {
    private String rconCmd;
    private String cardDesc;

    public String getRconCmd() {
        return rconCmd;
    }

    public void setRconCmd(String rconCmd) {
        this.rconCmd = rconCmd;
    }

    public String getCardDesc() {
        return cardDesc;
    }

    public void setCardDesc(String cardDesc) {
        this.cardDesc = cardDesc;
    }

}
