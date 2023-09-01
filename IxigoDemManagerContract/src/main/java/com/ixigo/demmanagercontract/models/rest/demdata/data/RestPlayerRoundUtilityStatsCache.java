package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPlayerRoundUtilityStatsCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long grenades_thrown = null;
	private Long smokes_thrown = null;
	private Long round = null;
	private Long flashes_thrown = null;
	private Long match_id = null;
	private Long inferno_thrown = null;

}
