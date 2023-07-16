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
public class Round_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String match_filename = "";
	private Long reasonendround = null;
	private Long roundnumber = null;
	private Long winnerside = null;

}
