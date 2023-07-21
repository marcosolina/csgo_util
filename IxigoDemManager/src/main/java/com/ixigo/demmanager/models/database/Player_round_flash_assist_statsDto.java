package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_round_flash_assist_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long round = null;
	private Long flashassists = null;
	private Long match_id = null;

}
