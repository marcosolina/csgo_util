package com.ixigo.demmanager.models.database;

import java.time.LocalDateTime;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
public class Match_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String match_filename = "";
	private Long match_id = null;
	private String mapname = "";
	private LocalDateTime match_date = null;

}
