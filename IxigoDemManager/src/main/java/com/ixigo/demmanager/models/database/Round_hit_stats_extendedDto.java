package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Round_hit_stats_extendedDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long hits = null;
	private Long stomach_hits = null;
	private String weapon = "";
	private Long total_damage = null;
	private Long leg_hits = null;
	private Long headshots = null;
	private Long round = null;
	private Long arm_hits = null;
	private Long match_id = null;
	private Long chest_hits = null;

}
