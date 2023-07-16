package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class SvcRoundShotEvent {
	private String eventType;
	private BigDecimal time;
	private String steamID;
	private Integer round;
	private String weapon;
	private String fileName;
}
