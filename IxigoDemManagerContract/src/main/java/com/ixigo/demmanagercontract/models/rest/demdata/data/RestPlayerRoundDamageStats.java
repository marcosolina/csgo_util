package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPlayerRoundDamageStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long he_damage = null;
	private Long round = null;
	private Long fire_damage = null;
	private Long match_id = null;
	private Long total_damage_armour = null;
	private Long opponents_flashed = null;
	private BigDecimal opponent_blindtime = BigDecimal.ZERO;
	private Long teammate_damage_health = null;
	private Long total_damage_health = null;
	private BigDecimal teammate_blindtime = BigDecimal.ZERO;

}
