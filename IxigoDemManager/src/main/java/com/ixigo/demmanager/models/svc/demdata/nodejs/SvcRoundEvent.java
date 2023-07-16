package com.ixigo.demmanager.models.svc.demdata.nodejs;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class SvcRoundEvent {
	private String eventType;
    private BigDecimal time;
    private String steamID;
    private Integer round;
	private String fileName;
}
