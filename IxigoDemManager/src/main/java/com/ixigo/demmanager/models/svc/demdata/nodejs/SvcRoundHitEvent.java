package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class SvcRoundHitEvent {
	private BigDecimal time;
	private BigDecimal blindTime;
	private String steamID;
	private Integer round;
	private String weapon;
	private String victimSteamId;
	private Integer hitGroup;
	private Integer damageHealth;
	private Integer damageArmour;
	private String fileName;
}
