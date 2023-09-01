package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_weapon_map_kills_cacheDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long kills = null;
	private String weapon = "";
	private Long headshots = null;
	private String mapname = "";

}
