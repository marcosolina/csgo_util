package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_match_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private BigDecimal kills = BigDecimal.ZERO;
	private BigDecimal ff = BigDecimal.ZERO;
	private BigDecimal bd = BigDecimal.ZERO;
	private Long _1v3 = null;
	private Long _1v2 = null;
	private Long _1v1 = null;
	private BigDecimal hr = BigDecimal.ZERO;
	private LocalDateTime match_date = null;
	private BigDecimal bp = BigDecimal.ZERO;
	private BigDecimal ud = BigDecimal.ZERO;
	private Long kasttotal = null;
	private Long score = null;
	private BigDecimal headshots = BigDecimal.ZERO;
	private BigDecimal assists = BigDecimal.ZERO;
	private Long _4k = null;
	private Long _2k = null;
	private String usernames = "";
	private Long _1v5 = null;
	private Long _1v4 = null;
	private BigDecimal headshot_percentage = BigDecimal.ZERO;
	private BigDecimal deaths = BigDecimal.ZERO;
	private BigDecimal ffd = BigDecimal.ZERO;
	private Long roundsplayed = null;
	private BigDecimal ek = BigDecimal.ZERO;
	private Long mvp = null;
	private Long match_id = null;
	private BigDecimal rwstotal = BigDecimal.ZERO;
	private String steamid = "";
	private BigDecimal td = BigDecimal.ZERO;
	private BigDecimal tda = BigDecimal.ZERO;
	private Long _5k = null;
	private Long _3k = null;
	private BigDecimal ebt = BigDecimal.ZERO;
	private BigDecimal tk = BigDecimal.ZERO;
	private BigDecimal kdr = BigDecimal.ZERO;
	private Long _1k = null;
	private BigDecimal tdh = BigDecimal.ZERO;
	private BigDecimal fbt = BigDecimal.ZERO;
	private BigDecimal fa = BigDecimal.ZERO;

}
