package com.ixigo.demmanager.models.database;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Player_kill_countDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private Long kill_count = null;
	private String victim = "";
	private String killer = "";

}
