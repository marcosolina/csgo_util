package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerRoundKillStatsCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long kills = null;
	private Long team_kills = null;
	private Long headshots = null;
	private Long round = null;
	private Long trade_kills = null;
	private Long match_id = null;
	private BigDecimal headshot_percentage = BigDecimal.ZERO;
	private Long entry_kills = null;

}
