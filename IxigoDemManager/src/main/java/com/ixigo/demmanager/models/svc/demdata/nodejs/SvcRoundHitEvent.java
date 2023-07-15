package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundHitEvent {
	private double time;
	private String steamID;
	private int round;
	private String weapon;
	private String victimSteamId;
	private int hitGroup;
	private int damageHealth;
	private int damageArmour;
	private int matchId;
}
