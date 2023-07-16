package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class SvcRoundKillEvent {
	private BigDecimal time;
    private String steamID;
    private String assister;
    private String flashAssister;
    private Boolean killerFlashed;
    private Integer round;
    private String weapon;
    private Boolean headshot;
    private String victimSteamId;
    private Boolean isFirstKill;
    private Boolean isTradeKill;
    private Boolean isTradeDeath;
	private String fileName;
}
