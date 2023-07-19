package com.ixigo.demmanager.models.svc.demdata.data;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Data
public class SvcRoundStats {
	private Integer roundNumber;
	private Integer winnerSide;
	private Integer reasonEndRound;
	private Long matchId;
}
