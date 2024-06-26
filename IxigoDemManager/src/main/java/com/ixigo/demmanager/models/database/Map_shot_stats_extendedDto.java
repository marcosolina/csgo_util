package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Map_shot_stats_extendedDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private String weapon = "";
	private Long shots_fired = null;
	private String mapname = "";

}
