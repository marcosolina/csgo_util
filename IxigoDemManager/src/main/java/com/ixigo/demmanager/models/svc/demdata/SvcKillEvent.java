package com.ixigo.demmanager.models.svc.demdata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcKillEvent {
	private String killerSteamId;
    private int round;
    private String weapon;
    private String victimSteamId;
}
