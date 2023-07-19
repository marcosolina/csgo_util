package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundKillEventsExtended implements Serializable {

	private static final long serialVersionUID = 1L;
	private String victimsteamid = "";
	private Boolean isfirstkill = null;
	private Long match_id = null;
	private BigDecimal eventtime = BigDecimal.ZERO;
	private Boolean istradekill = null;
	private String assister = "";
	private String steamid = "";
	private String weapon = "";
	private Long killer_team = null;
	private String flashassister = "";
	private Long round = null;
	private String killerflashed = "";
	private Boolean headshot = null;
	private Long victim_team = null;
	private Boolean istradedeath = null;

}
