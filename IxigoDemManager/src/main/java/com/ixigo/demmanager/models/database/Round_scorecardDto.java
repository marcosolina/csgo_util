package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Round_scorecardDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private Long total_equipment_value = null;
	private Long ct_score = null;
	private String round_type = "";
	private Long match_id = null;
	private Long round_end_reason = null;
	private Long team = null;
	private Long player_count = null;
	private Long round = null;
	private Long death_count = null;
	private Long t_score = null;
	private Long team2_score = null;
	private Long total_money_spent = null;
	private Long winner_team = null;
	private Long team1_score = null;

}
