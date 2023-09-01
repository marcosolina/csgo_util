package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Entry_kill_stats_cacheDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long total_rounds_t = null;
	private Long ekct_success = null;
	private Long total_rounds_ct = null;
	private Long ek_success = null;
	private Long ek_attempts = null;
	private Long ekt_attempts = null;
	private Long ekt_success = null;
	private Long ekct_attempts = null;
	private Long total_rounds = null;

}
