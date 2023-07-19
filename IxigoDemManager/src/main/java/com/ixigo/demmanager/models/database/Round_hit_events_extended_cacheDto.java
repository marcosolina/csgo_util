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
public class Round_hit_events_extended_cacheDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private String weapon = "";
	private Long damagearmour = null;
	private Long round = null;
	private String victimsteamid = "";
	private Long hitgroup = null;
	private Long match_id = null;
	private Long damagehealth = null;
	private BigDecimal eventtime = BigDecimal.ZERO;
	private Long attacker_team = null;
	private Long victim_team = null;
	private BigDecimal blindtime = BigDecimal.ZERO;

}
