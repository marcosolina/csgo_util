package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_round_utility_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long grenades_thrown = null;
	private Long smokes_thrown = null;
	private String match_filename = "";
	private Long round = null;
	private Long flashes_thrown = null;
	private Long inferno_thrown = null;

}
