package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundShotEvents implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private String weapon = "";
	private Long round = null;
	private Long match_id = null;
	private String eventtype = "";
	private BigDecimal eventtime = BigDecimal.ZERO;

}
