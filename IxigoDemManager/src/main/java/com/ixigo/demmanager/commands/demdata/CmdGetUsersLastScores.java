package com.ixigo.demmanager.commands.demdata;

import java.math.BigDecimal;
import java.util.List;

import com.ixigo.demmanagercontract.models.rest.demdata.RestUsersScores;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

public class CmdGetUsersLastScores implements WebCommandRequest<RestUsersScores> {
	private Integer counter;
	private List<String> usersIDs;
	private BigDecimal minPercPlayed;

	public CmdGetUsersLastScores(Integer counter, List<String> usersIDs, BigDecimal minPercPlayed) {
		super();
		this.counter = counter;
		this.usersIDs = usersIDs;
		this.minPercPlayed = minPercPlayed;
	}

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
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
