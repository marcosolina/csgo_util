package com.ixigo.demmanager.models.database;

import java.math.BigDecimal;

import com.ixigo.library.dto.IxigoDto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Getter
@Setter
@Accessors(chain = true)
public class Player_clutch_statsDto implements IxigoDto {

	private static final long serialVersionUID = 1L;
	private Long _1v5l = null;
	private Long _1v2l = null;
	private Long _1v1l = null;
	private BigDecimal _1v5p = BigDecimal.ZERO;
	private Long _1v4l = null;
	private Long _1v3l = null;
	private String steamid = "";
	private BigDecimal _1v2p = BigDecimal.ZERO;
	private Long _1vnl = null;
	private BigDecimal _1v1p = BigDecimal.ZERO;
	private BigDecimal _1v4p = BigDecimal.ZERO;
	private BigDecimal _1v3p = BigDecimal.ZERO;
	private Long _1v5w = null;
	private BigDecimal _1vnp = BigDecimal.ZERO;
	private Long _1v4w = null;
	private Long _1v1w = null;
	private Long _1v3w = null;
	private Long _1v2w = null;
	private Long _1vnw = null;

}
