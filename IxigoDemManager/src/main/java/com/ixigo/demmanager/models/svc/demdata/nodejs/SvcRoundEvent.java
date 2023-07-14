package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundEvent {
	private String eventType;
	private BigDecimal eventTime;
	private String steamID;
	private int round;
	private int matchId;
}
