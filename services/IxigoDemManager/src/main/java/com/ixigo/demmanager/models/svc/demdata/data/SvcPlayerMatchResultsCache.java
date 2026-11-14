package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerMatchResultsCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long rounds_on_team2 = null;
	private Long rounds_on_team1 = null;
	private Long team1_wins_as_ct = null;
	private Long match_id = null;
	private Long total_t_wins = null;
	private LocalDateTime match_date = null;
	private String steamid = "";
	private Long score_against = null;
	private String match_result = "";
	private Long score_for = null;
	private String last_round_team = "";
	private Long team2_wins_as_ct = null;
	private Long team2_wins_as_t = null;
	private Long total_ct_wins = null;
	private Long team1_wins_as_t = null;

}
