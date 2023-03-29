package com.ixigo.demmanager.commands.demdata;

import java.math.BigDecimal;
import java.util.List;

import com.ixigo.demmanagercontract.models.rest.demdata.RestUsersScores;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class CmdGetUsersLastScores implements WebCommandRequest<RestUsersScores> {
	private Integer numberOfMatches;
	private List<String> usersIDs;
	private BigDecimal minPercPlayed;

	public CmdGetUsersLastScores(Integer numberOfMatches, List<String> usersIDs, BigDecimal minPercPlayed) {
		super();
		this.numberOfMatches = numberOfMatches;
		this.usersIDs = usersIDs;
		this.minPercPlayed = minPercPlayed;
	}

	public Integer getNumberOfMatches() {
		return numberOfMatches;
	}

	public void setNumberOfMatches(Integer numberOfMatches) {
		this.numberOfMatches = numberOfMatches;
	}

	public List<String> getUsersIDs() {
		return usersIDs;
	}

	public void setUsersIDs(List<String> usersIDs) {
		this.usersIDs = usersIDs;
	}

	public BigDecimal getMinPercPlayed() {
		return minPercPlayed;
	}

	public void setMinPercPlayed(BigDecimal minPercPlayed) {
		this.minPercPlayed = minPercPlayed;
	}
}
