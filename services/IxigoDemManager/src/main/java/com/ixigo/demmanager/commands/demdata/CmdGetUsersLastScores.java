package com.ixigo.demmanager.commands.demdata;

import java.util.List;

import com.ixigo.demmanagercontract.models.rest.demdata.responses.RestUsersScoresResp;
import com.ixigo.library.mediators.web.interfaces.WebCommandRequest;

/**
 * Command dispatched to retrieve the scores of the specific players in the last
 * X matches
 * 
 * @author marco
 *
 */
public class CmdGetUsersLastScores implements WebCommandRequest<RestUsersScoresResp> {
	private Integer numberOfMatches;
	private List<String> usersIDs;

	public CmdGetUsersLastScores(Integer numberOfMatches, List<String> usersIDs) {
		super();
		this.numberOfMatches = numberOfMatches;
		this.usersIDs = usersIDs;
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
}
