package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class Player_round_kill_stats_cacheDto implements IxigoDto {

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
