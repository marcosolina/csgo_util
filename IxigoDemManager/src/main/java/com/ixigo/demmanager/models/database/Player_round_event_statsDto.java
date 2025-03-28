package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_round_event_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long bombs_defused = null;
	private Long round = null;
	private Long bombs_planted = null;
	private Long match_id = null;
	private Long hostages_rescued = null;

}
