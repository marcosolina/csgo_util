package com.ixigo.demmanager.commands.demdata;

/**
 * Command dispatched to retrieve the scores of the specific players in the last
 * X matches
 * 
 * @author marco
 *
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

 */