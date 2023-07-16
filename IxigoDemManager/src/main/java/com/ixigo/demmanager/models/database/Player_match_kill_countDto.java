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
public class Player_match_kill_countDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private Long match_id = null;
	private Long kill_count = null;
	private String victim = "";
	private String killer = "";

}
