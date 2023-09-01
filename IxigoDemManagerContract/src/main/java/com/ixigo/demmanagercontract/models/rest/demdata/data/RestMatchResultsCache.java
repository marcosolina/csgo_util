package com.ixigo.demmanagercontract.models.rest.demdata.data;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestMatchResultsCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long team1_wins_as_ct = null;
	private Long match_id = null;
	private Long team2_wins_as_ct = null;
	private Long team2_wins_as_t = null;
	private String mapname = "";
	private Long total_t_wins = null;
	private Long total_ct_wins = null;
	private Long team1_total_wins = null;
	private LocalDateTime match_date = null;
	private Long team1_wins_as_t = null;
	private Long team2_total_wins = null;

}
