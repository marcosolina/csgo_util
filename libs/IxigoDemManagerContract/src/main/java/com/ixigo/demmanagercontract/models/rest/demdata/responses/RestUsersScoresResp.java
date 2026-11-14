package com.ixigo.demmanagercontract.models.rest.demdata.responses;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ixigo.demmanagercontract.models.rest.demdata.data.RestPlayerMatchStatsExtended;

public class RestUsersScoresResp implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<String, List<RestPlayerMatchStatsExtended>> userScores;

	public Map<String, List<RestPlayerMatchStatsExtended>> getUserScores() {
		return userScores;
	}

	public void setUserScores(Map<String, List<RestPlayerMatchStatsExtended>> userScores) {
		this.userScores = userScores;
	}

}
