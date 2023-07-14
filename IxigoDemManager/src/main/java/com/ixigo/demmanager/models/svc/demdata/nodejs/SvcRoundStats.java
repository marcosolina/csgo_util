package com.ixigo.demmanager.models.svc.demdata.nodejs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundStats {
	private int roundNumber;
	private int winnerSide;
	private int reasonEndRound;
	private int matchId;
}
