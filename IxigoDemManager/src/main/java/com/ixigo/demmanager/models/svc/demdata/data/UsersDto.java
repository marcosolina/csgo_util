package com.ixigo.demmanager.models.svc.demdata.data;

import java.math.BigDecimal;
import com.ixigo.library.dto.IxigoDto;

@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class UsersDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private String user_name = "";
	private String steam_id = "";

}
