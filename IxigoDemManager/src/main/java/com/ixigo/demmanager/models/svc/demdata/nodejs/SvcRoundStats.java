package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class SvcRoundStats {
	private Integer roundNumber;
	private Integer winnerSide;
	private Integer reasonEndRound;
	private String fileName;
}
