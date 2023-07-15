package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_round_damage_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private String match_filename = "";
	private Long he_damage = null;
	private Long round = null;
	private Long fire_damage = null;
	private Long total_damage_armour = null;
	private Long opponents_flashed = null;
	private BigDecimal opponent_blindtime = BigDecimal.ZERO;
	private Long teammate_damage_health = null;
	private Long total_damage_health = null;
	private BigDecimal teammate_blindtime = BigDecimal.ZERO;

}
