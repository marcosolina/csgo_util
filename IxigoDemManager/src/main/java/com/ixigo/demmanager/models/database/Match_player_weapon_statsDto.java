package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Match_player_weapon_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private Long kills = null;
	private Long headshotkills = null;
	private BigDecimal damage_per_shot = BigDecimal.ZERO;
	private Long match_id = null;
	private BigDecimal accuracy = BigDecimal.ZERO;
	private String steamid = "";
	private Long hits = null;
	private String weapon = "";
	private Long total_damage = null;
	private BigDecimal damage_per_hit = BigDecimal.ZERO;
	private BigDecimal headshotkills_percentage = BigDecimal.ZERO;
	private Long shots_fired = null;
	private BigDecimal chest_hit_percentage = BigDecimal.ZERO;
	private BigDecimal leg_hit_percentage = BigDecimal.ZERO;
	private BigDecimal headshot_percentage = BigDecimal.ZERO;
	private BigDecimal stomach_hit_percentage = BigDecimal.ZERO;
	private String username = "";
	private BigDecimal arm_hit_percentage = BigDecimal.ZERO;

}
