package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundKillEvent {
	private int eventTime;
	private String steamID;
	private String assister;
	private String flashAssister;
	private String killerFlashed;
	private int round;
	private String weapon;
	private boolean headshot;
	private String victimSteamId;
	private boolean isFirstKill;
	private boolean isTradeKill;
	private boolean isTradeDeath;
	private int matchId;
}
