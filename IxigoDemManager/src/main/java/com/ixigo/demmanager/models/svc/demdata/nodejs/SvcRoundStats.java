package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class SvcRoundStats {
	private Integer roundNumber;
	private Integer winnerSide;
	private Integer reasonEndRound;
	private String fileName;
}
