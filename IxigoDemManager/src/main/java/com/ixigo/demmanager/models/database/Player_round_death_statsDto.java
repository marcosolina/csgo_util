package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_round_death_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long headshot_deaths = null;
	private Long round = null;
	private Long trade_deaths = null;
	private Long match_id = null;
	private Long team_deaths = null;
	private Long deaths = null;

}
