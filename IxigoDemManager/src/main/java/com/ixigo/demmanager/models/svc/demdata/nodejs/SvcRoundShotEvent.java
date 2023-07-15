package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundShotEvent {
	private String eventType;
    private double time;
    private String steamID;
    private int round;
    private String weapon;
    private int matchId;
}
