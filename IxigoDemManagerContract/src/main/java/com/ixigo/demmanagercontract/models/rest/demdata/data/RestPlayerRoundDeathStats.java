package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestPlayerRoundDeathStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long headshot_deaths = null;
	private Long round = null;
	private Long trade_deaths = null;
	private Long match_id = null;
	private Long team_deaths = null;
	private Long deaths = null;

}
