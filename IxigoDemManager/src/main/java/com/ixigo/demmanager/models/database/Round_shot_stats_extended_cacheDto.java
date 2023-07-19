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
public class Round_shot_stats_extended_cacheDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private String weapon = "";
	private Long round = null;
	private Long shots_fired = null;
	private Long match_id = null;

}
