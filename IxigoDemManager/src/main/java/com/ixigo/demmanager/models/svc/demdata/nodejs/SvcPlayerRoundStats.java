package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerRoundStats {
	private String userName;
    private String steamID;
    private int round;
    private int team;
    private BigDecimal clutchChance;
    private boolean clutchSuccess;
    private boolean survived;
    private int moneySpent;
    private int equipmentValue;
    private boolean mvp;
    private int matchId;
}
