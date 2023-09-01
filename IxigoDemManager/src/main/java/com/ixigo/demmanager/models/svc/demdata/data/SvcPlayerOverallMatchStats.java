package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerOverallMatchStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long wins = null;
	private Long loss = null;
	private BigDecimal averagewinscore = BigDecimal.ZERO;
	private BigDecimal winlossratio = BigDecimal.ZERO;

}
