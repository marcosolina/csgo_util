package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class Player_weapon_match_killsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long kills = null;
	private String weapon = "";
	private Long headshots = null;
	private Long match_id = null;

}
