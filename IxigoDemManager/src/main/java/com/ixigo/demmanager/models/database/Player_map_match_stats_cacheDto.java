package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_map_match_stats_cacheDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long wins = null;
	private Long loss = null;
	private BigDecimal averagewinscore = BigDecimal.ZERO;
	private BigDecimal winlossratio = BigDecimal.ZERO;
	private String mapname = "";

}
