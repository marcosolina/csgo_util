package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Round_stats_extended_cacheDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private Long reasonendround = null;
	private Long match_id = null;
	private Long roundnumber = null;
	private BigDecimal total_damage_winners = BigDecimal.ZERO;
	private Long winnerside = null;

}
