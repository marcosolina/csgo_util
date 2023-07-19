package com.ixigo.demmanager.models.svc.demdata.data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SvcPlayerRoundFlashAssistStatsCache implements Serializable {

	private static final long serialVersionUID = 1L;
	private String steamid = "";
	private Long round = null;
	private Long flashassists = null;
	private Long match_id = null;

}
