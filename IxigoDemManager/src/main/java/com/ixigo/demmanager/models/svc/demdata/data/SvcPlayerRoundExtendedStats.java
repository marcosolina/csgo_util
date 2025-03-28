package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerRoundExtendedStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long kills = null;
	private Long grenades_thrown = null;
	private Long headshot_deaths = null;
	private Long bombs_defused = null;
	private Long trade_kills = null;
	private Long kast = null;
	private BigDecimal opponent_blindtime = BigDecimal.ZERO;
	private Long hostages_rescued = null;
	private BigDecimal rws = BigDecimal.ZERO;
	private Long smokes_thrown = null;
	private Long headshots = null;
	private Long assists = null;
	private BigDecimal clutchchance = BigDecimal.ZERO;
	private Long bombs_planted = null;
	private Long total_damage_armour = null;
	private Long equipmentvalue = null;
	private Long team_deaths = null;
	private Long entry_kills = null;
	private BigDecimal headshot_percentage = BigDecimal.ZERO;
	private Long deaths = null;
	private Long team_kills = null;
	private Long moneyspent = null;
	private Boolean clutchsuccess = null;
	private Boolean mvp = null;
	private Long match_id = null;
	private Boolean survived = null;
	private Long team = null;
	private Long inferno_thrown = null;
	private BigDecimal teammate_blindtime = BigDecimal.ZERO;
	private String steamid = "";
	private Long he_damage = null;
	private Long round = null;
	private Long trade_deaths = null;
	private Long fire_damage = null;
	private Long flashassists = null;
	private Long flashes_thrown = null;
	private Long opponents_flashed = null;
	private Long teammate_damage_health = null;
	private Long total_damage_health = null;

}
