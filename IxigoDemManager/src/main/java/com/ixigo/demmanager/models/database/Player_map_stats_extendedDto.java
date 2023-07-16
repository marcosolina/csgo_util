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
public class Player_map_stats_extendedDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private BigDecimal kills = BigDecimal.ZERO;
	private BigDecimal ff = BigDecimal.ZERO;
	private BigDecimal hltv_rating = BigDecimal.ZERO;
	private BigDecimal bd = BigDecimal.ZERO;
	private BigDecimal _1v3 = BigDecimal.ZERO;
	private BigDecimal _1v2 = BigDecimal.ZERO;
	private BigDecimal kast = BigDecimal.ZERO;
	private BigDecimal _1v1 = BigDecimal.ZERO;
	private BigDecimal hr = BigDecimal.ZERO;
	private String mapname = "";
	private BigDecimal bp = BigDecimal.ZERO;
	private BigDecimal ud = BigDecimal.ZERO;
	private BigDecimal rws = BigDecimal.ZERO;
	private BigDecimal headshots = BigDecimal.ZERO;
	private BigDecimal assists = BigDecimal.ZERO;
	private BigDecimal _4k = BigDecimal.ZERO;
	private BigDecimal _2k = BigDecimal.ZERO;
	private String usernames = "";
	private BigDecimal _1v5 = BigDecimal.ZERO;
	private BigDecimal _1v4 = BigDecimal.ZERO;
	private BigDecimal headshot_percentage = BigDecimal.ZERO;
	private BigDecimal deaths = BigDecimal.ZERO;
	private BigDecimal ffd = BigDecimal.ZERO;
	private BigDecimal ek = BigDecimal.ZERO;
	private BigDecimal mvp = BigDecimal.ZERO;
	private BigDecimal dpr = BigDecimal.ZERO;
	private BigDecimal kpr = BigDecimal.ZERO;
	private Long matches = null;
	private BigDecimal adr = BigDecimal.ZERO;
	private String steamid = "";
	private BigDecimal td = BigDecimal.ZERO;
	private BigDecimal tda = BigDecimal.ZERO;
	private BigDecimal _5k = BigDecimal.ZERO;
	private BigDecimal _3k = BigDecimal.ZERO;
	private BigDecimal ebt = BigDecimal.ZERO;
	private BigDecimal tk = BigDecimal.ZERO;
	private BigDecimal kdr = BigDecimal.ZERO;
	private BigDecimal _1k = BigDecimal.ZERO;
	private BigDecimal tdh = BigDecimal.ZERO;
	private BigDecimal fbt = BigDecimal.ZERO;
	private BigDecimal fa = BigDecimal.ZERO;
	private BigDecimal rounds = BigDecimal.ZERO;

}
