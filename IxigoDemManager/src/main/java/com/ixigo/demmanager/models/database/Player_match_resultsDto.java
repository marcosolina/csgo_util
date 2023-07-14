package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_match_resultsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long score_against = null;
	private Long rounds_on_team2 = null;
	private Long rounds_on_team1 = null;
	private String match_result = "";
	private Long score_for = null;
	private Long match_id = null;
	private String last_round_team = "";

}
