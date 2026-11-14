package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestRoundPlayerWeaponStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal damage_per_shot = BigDecimal.ZERO;
	private Long match_id = null;
	private BigDecimal accuracy = BigDecimal.ZERO;
	private String steamid = "";
	private Long hits = null;
	private String weapon = "";
	private Long total_damage = null;
	private Long round = null;
	private BigDecimal damage_per_hit = BigDecimal.ZERO;
	private Long shots_fired = null;
	private BigDecimal chest_hit_percentage = BigDecimal.ZERO;
	private BigDecimal leg_hit_percentage = BigDecimal.ZERO;
	private BigDecimal headshot_percentage = BigDecimal.ZERO;
	private BigDecimal stomach_hit_percentage = BigDecimal.ZERO;
	private String username = "";
	private BigDecimal arm_hit_percentage = BigDecimal.ZERO;

}
