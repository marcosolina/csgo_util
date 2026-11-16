package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerRoundEventStatsCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long bombs_defused = null;
	private Long round = null;
	private Long bombs_planted = null;
	private Long match_id = null;
	private Long hostages_rescued = null;

}
