package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class Match_results_cacheDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private Long team1_wins_as_ct = null;
	private Long match_id = null;
	private Long team2_wins_as_ct = null;
	private Long team2_wins_as_t = null;
	private String mapname = "";
	private Long total_t_wins = null;
	private Long total_ct_wins = null;
	private Long team1_total_wins = null;
	private LocalDateTime match_date = null;
	private Long team1_wins_as_t = null;
	private Long team2_total_wins = null;

}
