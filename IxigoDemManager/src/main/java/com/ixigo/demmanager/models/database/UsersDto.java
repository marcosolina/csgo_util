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
public class UsersDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String user_name = "";
	private String steam_id = "";

}
