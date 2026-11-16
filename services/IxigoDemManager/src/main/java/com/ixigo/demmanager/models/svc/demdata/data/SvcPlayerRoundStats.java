package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerRoundStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long round = null;
	private BigDecimal clutchchance = BigDecimal.ZERO;
	private Long moneyspent = null;
	private Boolean clutchsuccess = null;
	private Boolean mvp = null;
	private Long match_id = null;
	private Long equipmentvalue = null;
	private Long team = null;
	private Boolean survived = null;
	private String username = "";

}
