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
public class Player_round_utility_stats_cacheDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long grenades_thrown = null;
	private Long smokes_thrown = null;
	private Long round = null;
	private Long flashes_thrown = null;
	private Long match_id = null;
	private Long inferno_thrown = null;

}