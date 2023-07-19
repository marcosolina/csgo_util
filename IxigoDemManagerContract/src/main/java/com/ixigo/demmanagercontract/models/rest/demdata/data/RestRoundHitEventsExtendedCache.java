package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestRoundHitEventsExtendedCache implements Serializable {

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
