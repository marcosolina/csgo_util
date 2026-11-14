package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundStatsExtendedCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long reasonendround = null;
	private Long match_id = null;
	private Long roundnumber = null;
	private BigDecimal total_damage_winners = BigDecimal.ZERO;
	private Long winnerside = null;

}
