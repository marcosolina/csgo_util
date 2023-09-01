package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcRoundStats implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long reasonendround = null;
	private Long match_id = null;
	private Long roundnumber = null;
	private Long winnerside = null;

}
